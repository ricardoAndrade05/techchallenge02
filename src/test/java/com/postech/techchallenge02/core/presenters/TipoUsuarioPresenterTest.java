package com.postech.techchallenge02.core.presenters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

@DisplayName("TipoUsuarioPresenter")
class TipoUsuarioPresenterTest {

    private static final Long ID_TIPO_USUARIO = 1L;
    private static final Long ID_RESTAURANTE = 10L;
    private static final String NOME_RESTAURANTE = "Restaurante Sabor & Arte";
    private static final String NOME_TIPO = "Gerente";
    private static final TipoUsuarioEnun TIPO = TipoUsuarioEnun.DONO;

    private Restaurante restaurante;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        restaurante = Restaurante.criar(ID_RESTAURANTE, NOME_RESTAURANTE);
        tipoUsuario = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, NOME_TIPO, TIPO);
    }

    @Nested
    @DisplayName("toDTO()")
    class ToDTO {

        @Test
        @DisplayName("Deve converter TipoUsuario para TipoUsuarioDTO mantendo todos os campos")
        void deveConverterParaDTO_comTodosOsCamposPreenchidos() {
            TipoUsuarioDTO dto = TipoUsuarioPresenter.toDTO(tipoUsuario);

            assertThat(dto).isNotNull();
            assertThat(dto.id()).isEqualTo(ID_TIPO_USUARIO);
            assertThat(dto.nome()).isEqualTo(NOME_TIPO);
            assertThat(dto.tipo()).isEqualTo(TIPO);
        }

        @Test
        @DisplayName("Deve preencher o RestauranteDTO com id e nome do restaurante")
        void devePreencherRestauranteDTO_comIdENomeCorretos() {
            TipoUsuarioDTO dto = TipoUsuarioPresenter.toDTO(tipoUsuario);

            assertThat(dto.restaurante()).isNotNull();
            assertThat(dto.restaurante().id()).isEqualTo(ID_RESTAURANTE);
            assertThat(dto.restaurante().nome()).isEqualTo(NOME_RESTAURANTE);
        }

        @Test
        @DisplayName("Deve converter corretamente para o tipo CLIENTE")
        void deveConverterParaDTO_comTipoCliente() {
            TipoUsuario tipoCliente = TipoUsuario.restaurar(2L, restaurante, "Comprador", TipoUsuarioEnun.CLIENTE);

            TipoUsuarioDTO dto = TipoUsuarioPresenter.toDTO(tipoCliente);

            assertThat(dto.tipo()).isEqualTo(TipoUsuarioEnun.CLIENTE);
            assertThat(dto.nome()).isEqualTo("Comprador");
            assertThat(dto.id()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Deve converter TipoUsuario com id nulo (recém-criado) sem erros")
        void deveConverterParaDTO_comIdNulo() {
            TipoUsuario tipoSemId = TipoUsuario.criar(restaurante, NOME_TIPO, TIPO);

            TipoUsuarioDTO dto = TipoUsuarioPresenter.toDTO(tipoSemId);

            assertThat(dto).isNotNull();
            assertThat(dto.id()).isNull();
            assertThat(dto.nome()).isEqualTo(NOME_TIPO);
        }

        @Test
        @DisplayName("Deve lançar NullPointerException quando TipoUsuario for nulo")
        void deveLancarExcecao_quandoTipoUsuarioNulo() {
            assertThatThrownBy(() -> TipoUsuarioPresenter.toDTO(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("toDTOs()")
    class ToDTOs {

        @Test
        @DisplayName("Deve converter uma lista com um único elemento corretamente")
        void deveConverterListaComUmElemento() {
            List<TipoUsuario> tipos = List.of(tipoUsuario);

            List<TipoUsuarioDTO> dtos = TipoUsuarioPresenter.toDTOs(tipos);

            assertThat(dtos).hasSize(1);
            assertThat(dtos.get(0).id()).isEqualTo(ID_TIPO_USUARIO);
            assertThat(dtos.get(0).nome()).isEqualTo(NOME_TIPO);
        }

        @Test
        @DisplayName("Deve converter uma lista com múltiplos elementos mantendo a ordem")
        void deveConverterListaComMultiplosElementos_mantendoOrdem() {
            TipoUsuario tipoGerente = TipoUsuario.restaurar(1L, restaurante, "Gerente", TipoUsuarioEnun.DONO);
            TipoUsuario tipoCaixa = TipoUsuario.restaurar(2L, restaurante, "Caixa", TipoUsuarioEnun.DONO);
            TipoUsuario tipoAtendente = TipoUsuario.restaurar(3L, restaurante, "Atendente", TipoUsuarioEnun.DONO);

            List<TipoUsuarioDTO> dtos = TipoUsuarioPresenter.toDTOs(List.of(tipoGerente, tipoCaixa, tipoAtendente));

            assertThat(dtos).hasSize(3);
            assertThat(dtos.get(0).nome()).isEqualTo("Gerente");
            assertThat(dtos.get(1).nome()).isEqualTo("Caixa");
            assertThat(dtos.get(2).nome()).isEqualTo("Atendente");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando a lista de entrada for vazia")
        void deveRetornarListaVazia_quandoListaDeEntradaVazia() {
            List<TipoUsuarioDTO> dtos = TipoUsuarioPresenter.toDTOs(List.of());

            assertThat(dtos).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Deve preencher o RestauranteDTO em todos os elementos convertidos")
        void devePreencherRestauranteDTOEmTodosOsElementos() {
            TipoUsuario tipoA = TipoUsuario.restaurar(1L, restaurante, "Gerente", TipoUsuarioEnun.DONO);
            TipoUsuario tipoB = TipoUsuario.restaurar(2L, restaurante, "Caixa", TipoUsuarioEnun.DONO);

            List<TipoUsuarioDTO> dtos = TipoUsuarioPresenter.toDTOs(List.of(tipoA, tipoB));

            assertThat(dtos).allSatisfy(dto -> {
                assertThat(dto.restaurante()).isNotNull();
                assertThat(dto.restaurante().id()).isEqualTo(ID_RESTAURANTE);
                assertThat(dto.restaurante().nome()).isEqualTo(NOME_RESTAURANTE);
            });
        }

        @Test
        @DisplayName("Deve lançar NullPointerException quando a lista de entrada for nula")
        void deveLancarExcecao_quandoListaNula() {
            assertThatThrownBy(() -> TipoUsuarioPresenter.toDTOs(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
