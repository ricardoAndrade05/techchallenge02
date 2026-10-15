package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioNaoEncontradoException;
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
        restaurante = Restaurante.criar(ID_RESTAURANTE, "Restaurante Teste");
        tipoUsuario = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, "Gerente", TipoUsuarioEnun.DONO);
        TipoUsuario tipoUsuarioDono = TipoUsuario.criar(restaurante, "Tipo Dono", TipoUsuarioEnun.DONO);
        usuarioDono = Usuario.restaurar(ID_USUARIO_LOGADO, "Maria Dona", tipoUsuarioDono);
    }

    @Nested
    @DisplayName("Dado dados de entrada inválidos")
    class QuandoDadosInvalidos {

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idTipoUsuario for nulo")
        void deveLancarExcecao_quandoIdTipoUsuarioNulo() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(null, NOVO_NOME, ID_USUARIO_LOGADO);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Tipo de usuário inválido");

            verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idUsuarioLogado for nulo")
        void deveLancarExcecao_quandoIdUsuarioLogadoNulo() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, null);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Usuário logado inválido");

            verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException com ambos os erros quando ambos os ids forem nulos")
        void deveLancarExcecao_quandoAmbosOsIdsNulos() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(null, NOVO_NOME, null);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Tipo de usuário inválido")
                    .hasMessageContaining("Usuário logado inválido");
        }
    }

    @Nested
    @DisplayName("Dado que o usuário logado não existe ou não tem permissão")
    class QuandoUsuarioProblemas {

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontradoException quando o usuário logado não for encontrado")
        void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(UsuarioNaoEncontradoException.class);

            verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
        }

        @Test
        @DisplayName("Deve lançar UsuarioSemPermissaoException quando o usuário for do tipo CLIENTE")
        void deveLancarExcecao_quandoUsuarioSemPermissao() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO);
            TipoUsuario tipoCliente = TipoUsuario.criar(restaurante, "Cliente Comum", TipoUsuarioEnun.CLIENTE);
            Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_LOGADO, "João Cliente", tipoCliente);

            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioCliente));

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(UsuarioSemPermissaoException.class);

            verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
        }
    }

    @Nested
    @DisplayName("Dado que o tipo de usuário não existe ou já existe com o novo nome")
    class QuandoTipoUsuarioProblemas {

        @Test
        @DisplayName("Deve lançar TipoUsuarioNaoEncontradoException quando o tipo de usuário não for encontrado")
        void deveLancarExcecao_quandoTipoUsuarioNaoEncontrado() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(TipoUsuarioNaoEncontradoException.class);

            verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
        }

        @Test
        @DisplayName("Deve lançar TipoUsuarioExistenteException quando já existe tipo com o novo nome no restaurante")
        void deveLancarExcecao_quandoTipoJaExisteComNovoNome() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioGateway.existeTipoUsuario(NOVO_NOME, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(true);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(TipoUsuarioExistenteException.class);

            verify(tipoUsuarioGateway, never()).atualizarTipoUsuario(any());
        }
    }

    @Nested
    @DisplayName("Dado que todos os dados são válidos")
    class QuandoDadosValidos {

        @Test
        @DisplayName("Deve atualizar o tipo de usuário com sucesso e retornar o tipo atualizado")
        void deveAtualizarComSucesso_quandoDadosValidos() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO);
            TipoUsuario tipoAtualizado = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, NOVO_NOME, TipoUsuarioEnun.DONO);

            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioGateway.existeTipoUsuario(NOVO_NOME, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(false);
            when(tipoUsuarioGateway.atualizarTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoAtualizado);

            TipoUsuario resultado = useCase.execute(dto);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(ID_TIPO_USUARIO);
            assertThat(resultado.getNome()).isEqualTo(NOVO_NOME);
        }

        @Test
        @DisplayName("Deve invocar o gateway de atualização de tipo de usuário")
        void deveInvocarGatewayDeAtualizacao_quandoDadosValidos() {
            AtualizarTipoUsuarioDTO dto = new AtualizarTipoUsuarioDTO(ID_TIPO_USUARIO, NOVO_NOME, ID_USUARIO_LOGADO);
            TipoUsuario tipoAtualizado = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, NOVO_NOME, TipoUsuarioEnun.DONO);

            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO)).thenReturn(Optional.of(usuarioDono));
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioGateway.existeTipoUsuario(NOVO_NOME, ID_RESTAURANTE, TipoUsuarioEnun.DONO)).thenReturn(false);
            when(tipoUsuarioGateway.atualizarTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoAtualizado);

            useCase.execute(dto);

            verify(tipoUsuarioGateway).atualizarTipoUsuario(any(TipoUsuario.class));
        }
    }
}
