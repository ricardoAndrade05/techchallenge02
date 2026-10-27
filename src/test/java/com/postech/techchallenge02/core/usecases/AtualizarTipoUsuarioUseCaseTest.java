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

import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarTipoUsuarioUseCase")
class AtualizarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private AtualizarTipoUsuarioUseCase useCase;

    private static final Long ID_TIPO_USUARIO = 1L;
    private static final Long ID_USUARIO_LOGADO = 10L;
    private static final Long ID_RESTAURANTE = 100L;
    private static final String NOVO_NOME = "Gerente Sênior";

    private Restaurante restaurante;
    private TipoUsuario tipoUsuario;
    private Usuario usuarioDono;

    @BeforeEach
    void setUp() {
        useCase = AtualizarTipoUsuarioUseCase.getInstance(tipoUsuarioGateway, usuarioGateway);
        restaurante = Restaurante.restaurar(ID_RESTAURANTE, "Restaurante Teste", null, null, null, null, null);
        tipoUsuario = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, "Gerente", CategoriaEnun.DONO);
        TipoUsuario tipoUsuarioDono = TipoUsuario.criar(restaurante, "Tipo Dono", CategoriaEnun.DONO);
        usuarioDono = Usuario.restaurar(ID_USUARIO_LOGADO, "Maria Dona", tipoUsuarioDono);
    }

    // --- Validação de Dados de Entrada ---

    @Test
    @DisplayName("validarDados - Deve lançar DadosInvalidosException quando idTipoUsuario for nulo")
    void deveLancarExcecao_quandoIdTipoUsuarioNulo() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(null, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Tipo de usuário inválido");

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    @Test
    @DisplayName("validarDados - Deve lançar DadosInvalidosException quando idUsuarioLogado for nulo")
    void deveLancarExcecao_quandoIdUsuarioLogadoNulo() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, null, ID_RESTAURANTE);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Usuário logado inválido");

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    @Test
    @DisplayName("validarDados - Deve lançar DadosInvalidosException com ambos os erros quando ambos os ids forem nulos")
    void deveLancarExcecao_quandoAmbosOsIdsNulos() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(null, NOVO_NOME, null, ID_RESTAURANTE);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Tipo de usuário inválido")
                .hasMessageContaining("Usuário logado inválido");
    }

    // --- Permissões do Usuário ---

    @Test
    @DisplayName("permissaoUsuario - Deve lançar RecursoNaoEncontradoException quando o usuário logado não for encontrado")
    void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    @Test
    @DisplayName("permissaoUsuario - Deve lançar UsuarioSemPermissaoException quando o usuário for do tipo CLIENTE")
    void deveLancarExcecao_quandoUsuarioSemPermissao() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        TipoUsuario tipoCliente = TipoUsuario.criar(restaurante, "Cliente Comum", CategoriaEnun.CLIENTE);
        Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_LOGADO, "João Cliente", tipoCliente);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioCliente));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class);

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    @Test
    @DisplayName("permissaoUsuario - Deve lançar UsuarioSemPermissaoException quando o usuário não possuir tipoUsuario definido")
    void deveLancarExcecao_quandoUsuarioSemTipoDefinido() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        Usuario usuarioSemTipo = Usuario.restaurar(ID_USUARIO_LOGADO, "Usuário Sem Perfil", null);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioSemTipo));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class);

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    // --- Regras de Negócio do TipoUsuario ---

    @Test
    @DisplayName("regraTipoUsuario - Deve lançar TipoUsuarioNaoEncontradoException quando o tipo de usuário não for encontrado")
    void deveLancarExcecao_quandoTipoUsuarioNaoEncontrado() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    @Test
    @DisplayName("regraTipoUsuario - Deve lançar TipoUsuarioExistenteException quando já existe tipo com o novo nome no restaurante")
    void deveLancarExcecao_quandoTipoJaExisteComNovoNome() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
        when(tipoUsuarioGateway.existeTipoUsuario(NOVO_NOME, ID_RESTAURANTE, CategoriaEnun.DONO)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(TipoUsuarioExistenteException.class);

        verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
    }

    // --- Execução com Sucesso ---

    @Test
    @DisplayName("execute - Deve atualizar o tipo de usuário com sucesso e retornar o tipo atualizado")
    void deveAtualizarComSucesso_quandoDadosValidos() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        TipoUsuario tipoAtualizado = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, NOVO_NOME, CategoriaEnun.DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
        when(tipoUsuarioGateway.existeTipoUsuario(NOVO_NOME, ID_RESTAURANTE, CategoriaEnun.DONO)).thenReturn(false);
        when(tipoUsuarioGateway.atualizarTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoAtualizado);

        TipoUsuario resultado = useCase.execute(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(ID_TIPO_USUARIO);
        assertThat(resultado.getNome()).isEqualTo(NOVO_NOME);
    }

    @Test
    @DisplayName("execute - Deve invocar o gateway de atualização de tipo de usuário")
    void deveInvocarGatewayDeAtualizacao_quandoDadosValidos() {
        AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO, ID_RESTAURANTE);
        TipoUsuario tipoAtualizado = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, NOVO_NOME, CategoriaEnun.DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
        when(tipoUsuarioGateway.existeTipoUsuario(NOVO_NOME, ID_RESTAURANTE, CategoriaEnun.DONO)).thenReturn(false);
        when(tipoUsuarioGateway.atualizarTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoAtualizado);

        useCase.execute(dto);

        verify(tipoUsuarioGateway).atualizarTipoUsuario(any(TipoUsuario.class));
    }
}