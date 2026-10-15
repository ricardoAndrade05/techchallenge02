package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioNaoEncontradoException;
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
        restaurante = Restaurante.criar(ID_RESTAURANTE, "Restaurante Teste");
        tipoUsuarioDono = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, "Gerente", TipoUsuarioEnun.DONO);
        usuarioDono = Usuario.restaurar(ID_USUARIO_LOGADO, "Maria Dona", tipoUsuarioDono);
    }

    @Nested
    @DisplayName("Dado dados de entrada inválidos")
    class QuandoDadosInvalidos {

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idUsuarioLogado for nulo")
        void deveLancarExcecao_quandoIdUsuarioLogadoNulo() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(null, ID_TIPO_USUARIO);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Usuário inválido");

            verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idTipoUsuario for nulo")
        void deveLancarExcecao_quandoIdTipoUsuarioNulo() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, null);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Tipo de usuário inválido");

            verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException com ambos erros quando os dois ids forem nulos")
        void deveLancarExcecao_quandoAmbosOsIdsNulos() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(null, null);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Usuário inválido")
                    .hasMessageContaining("Tipo de usuário inválido");
        }
    }

    @Nested
    @DisplayName("Dado que o usuário logado não existe ou não tem permissão")
    class QuandoUsuarioProblemas {

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontradoException quando o usuário não for encontrado")
        void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessageContaining(String.valueOf(ID_USUARIO_LOGADO));

            verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
        }

        @Test
        @DisplayName("Deve lançar UsuarioSemPermissaoException quando o usuário for do tipo CLIENTE")
        void deveLancarExcecao_quandoUsuarioClienteSemPermissao() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
            TipoUsuario tipoCliente = TipoUsuario.criar(restaurante, "Cliente Comum", TipoUsuarioEnun.CLIENTE);
            Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_LOGADO, "João Cliente", tipoCliente);

            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioCliente));

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(UsuarioSemPermissaoException.class)
                    .hasMessageContaining(String.valueOf(ID_USUARIO_LOGADO));

            verify(tipoUsuarioGateway, never()).excluirTipoUsuario(eq(ID_TIPO_USUARIO));
        }
    }

    @Nested
    @DisplayName("Dado que o tipo de usuário não existe")
    class QuandoTipoUsuarioNaoExiste {

        @Test
        @DisplayName("Deve lançar TipoUsuarioNaoEncontradoException quando o tipo não existir para o restaurante")
        void deveLancarExcecao_quandoTipoUsuarioNaoExiste() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.existeTipoUsuario(ID_TIPO_USUARIO, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(false);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(TipoUsuarioNaoEncontradoException.class)
                    .hasMessageContaining(String.valueOf(ID_TIPO_USUARIO));

            verify(tipoUsuarioGateway, never()).excluirTipoUsuario(ID_TIPO_USUARIO);
        }
    }

    @Nested
    @DisplayName("Dado que o tipo de usuário está vinculado a usuários")
    class QuandoTipoUsuarioVinculado {

        @Test
        @DisplayName("Deve lançar TipoUsuarioVinculadoException quando o tipo estiver em uso por usuários")
        void deveLancarExcecao_quandoTipoUsuarioVinculado() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.existeTipoUsuario(ID_TIPO_USUARIO, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(true);
            when(tipoUsuarioGateway.tipoUsuarioEstaVinculado(ID_TIPO_USUARIO)).thenReturn(true);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(TipoUsuarioVinculadoException.class);

            verify(tipoUsuarioGateway, never()).excluirTipoUsuario(ID_TIPO_USUARIO);
        }
    }

    @Nested
    @DisplayName("Dado que todos os dados são válidos para exclusão")
    class QuandoDadosValidos {

        @Test
        @DisplayName("Deve excluir o tipo de usuário com sucesso sem lançar exceção")
        void deveExcluirComSucesso_quandoDadosValidos() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.existeTipoUsuario(ID_TIPO_USUARIO, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(true);
            when(tipoUsuarioGateway.tipoUsuarioEstaVinculado(ID_TIPO_USUARIO)).thenReturn(false);

            assertThatCode(() -> useCase.execute(dto)).doesNotThrowAnyException();

            verify(tipoUsuarioGateway).excluirTipoUsuario(ID_TIPO_USUARIO);
        }

        @Test
        @DisplayName("Deve invocar o gateway de exclusão exatamente uma vez com o id correto")
        void deveInvocarGatewayDeExclusao_comIdCorreto() {
            ExcluirTipoUsuarioDTO dto = new ExcluirTipoUsuarioDTO(ID_USUARIO_LOGADO, ID_TIPO_USUARIO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.existeTipoUsuario(ID_TIPO_USUARIO, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(true);
            when(tipoUsuarioGateway.tipoUsuarioEstaVinculado(ID_TIPO_USUARIO)).thenReturn(false);

            useCase.execute(dto);

            verify(tipoUsuarioGateway).excluirTipoUsuario(ID_TIPO_USUARIO);
            verify(tipoUsuarioGateway).existeTipoUsuario(ID_TIPO_USUARIO, ID_RESTAURANTE, TipoUsuarioEnun.DONO);
            verify(tipoUsuarioGateway).tipoUsuarioEstaVinculado(ID_TIPO_USUARIO);
        }
    }
}
