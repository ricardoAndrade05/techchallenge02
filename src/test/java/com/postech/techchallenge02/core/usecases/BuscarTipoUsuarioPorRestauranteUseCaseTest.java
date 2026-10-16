package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarTipoUsuarioPorRestauranteUseCase")
class BuscarTipoUsuarioPorRestauranteUseCaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    private BuscarTipoUsuarioPorRestauranteUseCase useCase;

    private static final Long ID_RESTAURANTE = 1L;
    private static final Long ID_RESTAURANTE_INEXISTENTE = 999L;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        useCase = BuscarTipoUsuarioPorRestauranteUseCase.getInstance(tipoUsuarioGateway, restauranteGateway);
        restaurante = Restaurante.restaurar (ID_RESTAURANTE, "Restaurante Teste",null,null,null,null,null);
    }

    @Nested
    @DisplayName("Dado que o idRestaurante fornecido é inválido")
    class QuandoIdRestauranteInvalido {

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idRestaurante for nulo")
        void deveLancarExcecao_quandoIdRestauranteNulo() {
            Paginacao paginacao = new Paginacao(0, 10);

            assertThatThrownBy(() -> useCase.execute(null, paginacao))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Restaurante inválido");

            verify(restauranteGateway, never()).buscaRestaurantePorId(any());
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando idRestaurante for nulo na sobrecarga sem paginacao")
        void deveLancarExcecao_quandoIdRestauranteNuloSemPaginacao() {
            assertThatThrownBy(() -> useCase.execute(null))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Restaurante inválido");
        }
    }

    @Nested
    @DisplayName("Dado que o restaurante não existe")
    class QuandoRestauranteNaoExiste {

        @Test
        @DisplayName("Deve lançar RecursoNaoEncontradoException quando o restaurante não for encontrado")
        void deveLancarExcecao_quandoRestauranteNaoEncontrado() {
            Paginacao paginacao = new Paginacao(0, 10);
            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE_INEXISTENTE)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(ID_RESTAURANTE_INEXISTENTE, paginacao))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(tipoUsuarioGateway, never()).buscaTiposUsuarioPorRestaurante(any(), any());
        }
    }

    @Nested
    @DisplayName("Dado que o restaurante existe mas não possui tipos de usuário")
    class QuandoRestauranteSemTiposUsuario {

        @Test
        @DisplayName("Deve lançar TipoUsuarioNaoEncontradoException quando a lista de tipos estiver vazia")
        void deveLancarExcecao_quandoNaoHaTiposDeUsuario() {
            Paginacao paginacao = new Paginacao(0, 10);
            Pagina<TipoUsuario> paginaVazia = new Pagina<>(List.of(), 0, 10, 0, 0);

            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
            when(tipoUsuarioGateway.buscaTiposUsuarioPorRestaurante(eq(restaurante), any(Paginacao.class)))
                    .thenReturn(paginaVazia);

            assertThatThrownBy(() -> useCase.execute(ID_RESTAURANTE, paginacao))
                    .isInstanceOf(RecursoNaoEncontradoException.class)
                    .hasMessageContaining(String.valueOf(ID_RESTAURANTE));
        }
    }

    @Nested
    @DisplayName("Dado que o restaurante existe e possui tipos de usuário")
    class QuandoRestauranteComTiposUsuario {

        @Test
        @DisplayName("Deve retornar a página de tipos de usuário quando o restaurante tiver tipos cadastrados")
        void deveRetornarPagina_quandoRestaurantePossuiTiposDeUsuario() {
            Paginacao paginacao = new Paginacao(0, 10);
            TipoUsuario tipo = TipoUsuario.restaurar(1L, restaurante, "Gerente", CategoriaEnun.DONO);
            Pagina<TipoUsuario> paginaComItens = new Pagina<>(List.of(tipo), 0, 10, 1, 1);

            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
            when(tipoUsuarioGateway.buscaTiposUsuarioPorRestaurante(eq(restaurante), any(Paginacao.class)))
                    .thenReturn(paginaComItens);

            Pagina<TipoUsuario> resultado = useCase.execute(ID_RESTAURANTE, paginacao);

            assertThat(resultado).isNotNull();
            assertThat(resultado.estaVazia()).isFalse();
            assertThat(resultado.itens()).hasSize(1);
            assertThat(resultado.itens().get(0).getNome()).isEqualTo("Gerente");
        }

        @Test
        @DisplayName("Deve usar paginação padrão (página 0, tamanho 10) quando chamado sem paginação explícita")
        void deveUsarPaginacaoPadrao_quandoChamadoSemPaginacao() {
            TipoUsuario tipo = TipoUsuario.restaurar(1L, restaurante, "Gerente", CategoriaEnun.DONO);
            Pagina<TipoUsuario> paginaComItens = new Pagina<>(List.of(tipo), 0, 10, 1, 1);

            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
            when(tipoUsuarioGateway.buscaTiposUsuarioPorRestaurante(eq(restaurante), any(Paginacao.class)))
                    .thenReturn(paginaComItens);

            Pagina<TipoUsuario> resultado = useCase.execute(ID_RESTAURANTE);

            assertThat(resultado).isNotNull();
            assertThat(resultado.estaVazia()).isFalse();
        }

        @Test
        @DisplayName("Deve retornar múltiplos tipos de usuário quando o restaurante tiver vários cadastrados")
        void deveRetornarMultiplosTipos_quandoRestaurantePossuiVarios() {
            Paginacao paginacao = new Paginacao(0, 10);
            TipoUsuario tipoGerente = TipoUsuario.restaurar(1L, restaurante, "Gerente", CategoriaEnun.DONO);
            TipoUsuario tipoCaixa = TipoUsuario.restaurar(2L, restaurante, "Caixa", CategoriaEnun.DONO);
            Pagina<TipoUsuario> paginaComItens = new Pagina<>(List.of(tipoGerente, tipoCaixa), 0, 10, 2, 1);

            when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
            when(tipoUsuarioGateway.buscaTiposUsuarioPorRestaurante(eq(restaurante), any(Paginacao.class)))
                    .thenReturn(paginaComItens);

            Pagina<TipoUsuario> resultado = useCase.execute(ID_RESTAURANTE, paginacao);

            assertThat(resultado.itens()).hasSize(2);
            assertThat(resultado.totalItens()).isEqualTo(2);
        }
    }
}
