package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExcluirTipoUsuarioUseCase")
class ExcluirTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private ExcluirTipoUsuarioUseCase useCase;

    private static final Long ID_USUARIO_LOGADO = 10L;
    private static final Long ID_TIPO_USUARIO = 1L;
    private static final Long ID_RESTAURANTE = 100L;

    private Restaurante restaurante;
    private TipoUsuario tipoUsuarioDono;
    private Usuario usuarioDono;

    @BeforeEach
    void setUp() {
        useCase = ExcluirTipoUsuarioUseCase.getInstance(tipoUsuarioGateway, usuarioGateway);
        restaurante = Restaurante.restaurar(ID_RESTAURANTE, "Restaurante Teste", null, null, null, null, null);
        tipoUsuarioDono = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, "Gerente", CategoriaEnun.DONO);
        usuarioDono = Usuario.restaurar(ID_USUARIO_LOGADO, "Maria Dona", tipoUsuarioDono);
    }

    // --- Dados de Entrada Inválidos ---

    @Test
    @DisplayName("dadosInvalidos - Deve lançar DadosInvalidosException quando idUsuarioLogado for nulo")
    void deveLancarExcecao_quandoIdUsuarioLogadoNulo() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(null, ID_TIPO_USUARIO);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Usuário inválido");

        verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
    }

    @Test
    @DisplayName("dadosInvalidos - Deve lançar DadosInvalidosException quando idTipoUsuario for nulo")
    void deveLancarExcecao_quandoIdTipoUsuarioNulo() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, null);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Tipo de usuário inválido");

        verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
    }

    @Test
    @DisplayName("dadosInvalidos - Deve lançar DadosInvalidosException com ambos erros quando os dois ids forem nulos")
    void deveLancarExcecao_quandoAmbosOsIdsNulos() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(null, null);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Usuário inválido")
                .hasMessageContaining("Tipo de usuário inválido");
    }

    // --- Permissões do Usuário Logado ---

    @Test
    @DisplayName("permissaoUsuario - Deve lançar RecursoNaoEncontradoException quando o usuário não for encontrado")
    void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuário não encontrado/inexistente para esse restaurante.");

        verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
    }

    @Test
    @DisplayName("permissaoUsuario - Deve lançar UsuarioSemPermissaoException quando o usuário for do tipo CLIENTE")
    void deveLancarExcecao_quandoUsuarioClienteSemPermissao() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
        TipoUsuario tipoCliente = TipoUsuario.criar(restaurante, "Cliente Comum", CategoriaEnun.CLIENTE);
        Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_LOGADO, "João Cliente", tipoCliente);

        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioCliente));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining(String.valueOf(ID_USUARIO_LOGADO));

        verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
    }

    // --- TipoUsuario Inexistente ---

    @Test
    @DisplayName("tipoUsuarioInexistente - Deve lançar RecursoNaoEncontradoException quando o tipo não existir para o restaurante")
    void deveLancarExcecao_quandoTipoUsuarioNaoExiste() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining(String.valueOf(ID_TIPO_USUARIO));

        verify(tipoUsuarioGateway, never()).excluirTipoUsuario(ID_TIPO_USUARIO);
    }

    // --- TipoUsuario Vinculado ---

    @Test
    @DisplayName("tipoUsuarioVinculado - Deve lançar TipoUsuarioVinculadoException quando o tipo estiver em uso por usuários")
    void deveLancarExcecao_quandoTipoUsuarioVinculado() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.tipoUsuarioEstaVinculado(ID_TIPO_USUARIO)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(TipoUsuarioVinculadoException.class);

        verify(tipoUsuarioGateway, never()).excluirTipoUsuario(ID_TIPO_USUARIO);
    }

    // --- Execução com Sucesso ---

    @Test
    @DisplayName("execucaoSucesso - Deve excluir o tipo de usuário com sucesso sem lançar exceção")
    void deveExcluirComSucesso_quandoDadosValidos() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.tipoUsuarioEstaVinculado(ID_TIPO_USUARIO)).thenReturn(false);

        assertThatCode(() -> useCase.execute(dto)).doesNotThrowAnyException();

        verify(tipoUsuarioGateway).excluirTipoUsuario(ID_TIPO_USUARIO);
    }

    @Test
    @DisplayName("execucaoSucesso - Deve invocar o gateway de exclusão exatamente uma vez com o id correto")
    void deveInvocarGatewayDeExclusao_comIdCorreto() {
        ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.tipoUsuarioEstaVinculado(ID_TIPO_USUARIO)).thenReturn(false);

        useCase.execute(dto);

        verify(tipoUsuarioGateway).excluirTipoUsuario(ID_TIPO_USUARIO);
        verify(tipoUsuarioGateway).tipoUsuarioEstaVinculado(ID_TIPO_USUARIO);
    }
}