package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarTipoUsuarioUseCase")
class CadastrarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    private CadastrarTipoUsuarioUseCase useCase;

    private static final Long ID_RESTAURANTE = 1L;
    private static final Long ID_USUARIO_CRIADOR = 10L;
    private static final Long ID_TIPO_USUARIO_SALVO = 100L;
    private static final String NOME_TIPO = "Gerente";
    private static final TipoUsuarioEnun TIPO_USUARIO = TipoUsuarioEnun.DONO;

    @BeforeEach
    void setUp() {
        useCase = CadastrarTipoUsuarioUseCase.getInstance(tipoUsuarioGateway, usuarioGateway, restauranteGateway);
    }

    @Nested
    @DisplayName("Dado dados de entrada inválidos")
    class QuandoDadosInvalidos {

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idRestaurante for nulo")
        void deveLancarExcecao_quandoIdRestauranteNulo() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(null, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Restaurante inválido");

            verify(tipoUsuarioGateway, never()).cadastraTipoUsuario(any());
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idUsuarioCriador for nulo")
        void deveLancarExcecao_quandoIdUsuarioCriadorNulo() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, null);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Usuário criador inválido");

            verify(tipoUsuarioGateway, never()).cadastraTipoUsuario(any());
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException com ambos os erros quando idRestaurante e idUsuarioCriador forem nulos")
        void deveLancarExcecao_quandoAmbosOsIdsNulos() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(null, NOME_TIPO, TIPO_USUARIO, null);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Restaurante inválido")
                    .hasMessageContaining("Usuário criador inválido");
        }
    }

    @Nested
    @DisplayName("Dado que o tipo de usuário já existe")
    class QuandoTipoUsuarioJaExiste {

        @Test
        @DisplayName("Deve lançar TipoUsuarioExistenteException quando o tipo já estiver cadastrado")
        void deveLancarExcecao_quandoTipoJaExiste() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);
            when(tipoUsuarioGateway.existeTipoUsuario(NOME_TIPO, ID_RESTAURANTE, TIPO_USUARIO)).thenReturn(true);

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(TipoUsuarioExistenteException.class)
                    .hasMessageContaining(NOME_TIPO);

            verify(tipoUsuarioGateway, never()).cadastraTipoUsuario(any());
        }
    }

    @Nested
    @DisplayName("Dado que o usuário criador não existe ou não tem permissão")
    class QuandoUsuarioProblemas {

        @Test
        @DisplayName("Deve lançar RecursoNaoEncontradoException quando o usuário criador não for encontrado")
        void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);
            when(tipoUsuarioGateway.existeTipoUsuario(NOME_TIPO, ID_RESTAURANTE, TIPO_USUARIO)).thenReturn(false);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_CRIADOR)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(RecursoNaoEncontradoException.class)
                    .hasMessageContaining(String.valueOf(ID_USUARIO_CRIADOR));
        }

        @Test
        @DisplayName("Deve lançar UsuarioSemPermissaoException quando o usuário não tiver permissão para criar tipo")
        void deveLancarExcecao_quandoUsuarioSemPermissao() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);
            Restaurante restaurante = Restaurante.criar(ID_RESTAURANTE, "Restaurante Teste");
            TipoUsuario tipoUsuarioCliente = TipoUsuario.criar(restaurante, "Cliente Comum", TipoUsuarioEnun.CLIENTE);
            Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_CRIADOR, "João Cliente", tipoUsuarioCliente);

            when(tipoUsuarioGateway.existeTipoUsuario(NOME_TIPO, ID_RESTAURANTE, TIPO_USUARIO)).thenReturn(false);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_CRIADOR)).thenReturn(Optional.of(usuarioCliente));

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(UsuarioSemPermissaoException.class)
                    .hasMessageContaining(String.valueOf(ID_USUARIO_CRIADOR));
        }
    }

    @Nested
    @DisplayName("Dado que o restaurante não existe")
    class QuandoRestauranteNaoExiste {

        @Test
        @DisplayName("Deve lançar RecursoNaoEncontradoException quando o restaurante não for encontrado")
        void deveLancarExcecao_quandoRestauranteNaoEncontrado() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);
            Restaurante restaurante = Restaurante.criar(ID_RESTAURANTE, "Restaurante Teste");
            TipoUsuario tipoUsuarioDono = TipoUsuario.criar(restaurante, "Tipo Dono", TipoUsuarioEnun.DONO);
            Usuario usuarioDono = Usuario.restaurar(ID_USUARIO_CRIADOR, "Maria Dona", tipoUsuarioDono);

            when(tipoUsuarioGateway.existeTipoUsuario(NOME_TIPO, ID_RESTAURANTE, TIPO_USUARIO)).thenReturn(false);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_CRIADOR)).thenReturn(Optional.of(usuarioDono));
            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(dto))
                    .isInstanceOf(RecursoNaoEncontradoException.class)
                    .hasMessageContaining(String.valueOf(ID_RESTAURANTE));
        }
    }

    @Nested
    @DisplayName("Dado que todos os dados são válidos")
    class QuandoDadosValidos {

        @Test
        @DisplayName("Deve cadastrar o tipo de usuário com sucesso e retornar o tipo criado")
        void deveCadastrarComSucesso_quandoDadosValidos() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);
            Restaurante restaurante = Restaurante.criar(ID_RESTAURANTE, "Restaurante Teste");
            TipoUsuario tipoUsuarioDono = TipoUsuario.criar(restaurante, "Tipo Dono", TipoUsuarioEnun.DONO);
            Usuario usuarioDono = Usuario.restaurar(ID_USUARIO_CRIADOR, "Maria Dona", tipoUsuarioDono);
            TipoUsuario tipoUsuarioSalvo = TipoUsuario.restaurar(ID_TIPO_USUARIO_SALVO, restaurante, NOME_TIPO, TIPO_USUARIO);

            when(tipoUsuarioGateway.existeTipoUsuario(NOME_TIPO, ID_RESTAURANTE, TIPO_USUARIO)).thenReturn(false);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_CRIADOR)).thenReturn(Optional.of(usuarioDono));
            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
            when(tipoUsuarioGateway.cadastraTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoUsuarioSalvo);

            TipoUsuario resultado = useCase.execute(dto);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(ID_TIPO_USUARIO_SALVO);
            assertThat(resultado.getNome()).isEqualTo(NOME_TIPO);
            assertThat(resultado.getTipo()).isEqualTo(TIPO_USUARIO);
            verify(tipoUsuarioGateway).cadastraTipoUsuario(any(TipoUsuario.class));
        }

        @Test
        @DisplayName("Deve invocar o gateway de tipo de usuário para persistir")
        void deveInvocarGateway_quandoCadastroComSucesso() {
            NovoTipoUsuarioDTO dto = new NovoTipoUsuarioDTO(ID_RESTAURANTE, NOME_TIPO, TIPO_USUARIO, ID_USUARIO_CRIADOR);
            Restaurante restaurante = Restaurante.criar(ID_RESTAURANTE, "Restaurante Teste");
            TipoUsuario tipoUsuarioDono = TipoUsuario.criar(restaurante, "Tipo Dono", TipoUsuarioEnun.DONO);
            Usuario usuarioDono = Usuario.restaurar(ID_USUARIO_CRIADOR, "Maria Dona", tipoUsuarioDono);
            TipoUsuario tipoUsuarioSalvo = TipoUsuario.restaurar(ID_TIPO_USUARIO_SALVO, restaurante, NOME_TIPO, TIPO_USUARIO);

            when(tipoUsuarioGateway.existeTipoUsuario(eq(NOME_TIPO), eq(ID_RESTAURANTE), eq(TIPO_USUARIO))).thenReturn(false);
            when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_CRIADOR)).thenReturn(Optional.of(usuarioDono));
            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
            when(tipoUsuarioGateway.cadastraTipoUsuario(any())).thenReturn(tipoUsuarioSalvo);

            useCase.execute(dto);

            verify(tipoUsuarioGateway).existeTipoUsuario(NOME_TIPO, ID_RESTAURANTE, TIPO_USUARIO);
            verify(usuarioGateway).buscaUsuarioPorId(ID_USUARIO_CRIADOR);
            verify(restauranteGateway).buscaRestaurantePorId(ID_RESTAURANTE);
            verify(tipoUsuarioGateway).cadastraTipoUsuario(any(TipoUsuario.class));
        }
    }
}
