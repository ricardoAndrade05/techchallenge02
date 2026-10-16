package com.postech.techchallenge02.core.entities;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

@DisplayName("TipoUsuario")
class TipoUsuarioTest {

    private static final Long ID = 1L;
    private static final String NOME_VALIDO = "Gerente";
    private static final CategoriaEnun TIPO_VALIDO = CategoriaEnun.DONO;

    private Restaurante restauranteValido() {
        return Restaurante.restaurar(10L, "Restaurante Teste",null,null,null,null,null);
    }

    // -------------------------------------------------------------------------
    // TipoUsuario.criar
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("criar()")
    class Criar {

        @Test
        @DisplayName("Deve criar uma instância com id nulo quando os dados são válidos")
        void deveCriarComIdNulo_quandoDadosValidos() {
            Restaurante restaurante = restauranteValido();

            TipoUsuario tipo = TipoUsuario.criar(restaurante, NOME_VALIDO, TIPO_VALIDO);

            assertThat(tipo.getId()).isNull();
            assertThat(tipo.getNome()).isEqualTo(NOME_VALIDO);
            assertThat(tipo.getCategoria()).isEqualTo(TIPO_VALIDO);
            assertThat(tipo.getRestaurante()).isEqualTo(restaurante);
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando o restaurante for nulo")
        void deveLancarExcecao_quandoRestauranteNulo() {
            assertThatThrownBy(() -> TipoUsuario.criar(null, NOME_VALIDO, TIPO_VALIDO))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Restaurante inválido");
        }

        @ParameterizedTest(name = "nome = \"{0}\"")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve lançar DadosInvalidosException quando o nome for nulo, vazio ou em branco")
        void deveLancarExcecao_quandoNomeInvalido(String nomeInvalido) {
            assertThatThrownBy(() -> TipoUsuario.criar(restauranteValido(), nomeInvalido, TIPO_VALIDO))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Nome inválido");
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando o tipo de usuário for nulo")
        void deveLancarExcecao_quandoTipoNulo() {
            assertThatThrownBy(() -> TipoUsuario.criar(restauranteValido(), NOME_VALIDO, null))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Tipo de usuário inválido");
        }
    }

    // -------------------------------------------------------------------------
    // TipoUsuario.restaurar
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("restaurar()")
    class Restaurar {

        @Test
        @DisplayName("Deve restaurar uma instância com todos os dados incluindo id")
        void deveRestaurarComId_quandoDadosValidos() {
            Restaurante restaurante = restauranteValido();

            TipoUsuario tipo = TipoUsuario.restaurar(ID, restaurante, NOME_VALIDO, TIPO_VALIDO);

            assertThat(tipo.getId()).isEqualTo(ID);
            assertThat(tipo.getNome()).isEqualTo(NOME_VALIDO);
            assertThat(tipo.getCategoria()).isEqualTo(TIPO_VALIDO);
            assertThat(tipo.getRestaurante()).isEqualTo(restaurante);
        }

        @Test
        @DisplayName("Deve permitir restaurar com id nulo")
        void devePermitirRestaurarComIdNulo() {
            TipoUsuario tipo = TipoUsuario.restaurar(null, restauranteValido(), NOME_VALIDO, TIPO_VALIDO);

            assertThat(tipo.getId()).isNull();
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando o restaurante for nulo")
        void deveLancarExcecao_quandoRestauranteNulo() {
            assertThatThrownBy(() -> TipoUsuario.restaurar(ID, null, NOME_VALIDO, TIPO_VALIDO))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Restaurante inválido");
        }

        @ParameterizedTest(name = "nome = \"{0}\"")
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Deve lançar DadosInvalidosException quando o nome for nulo, vazio ou em branco")
        void deveLancarExcecao_quandoNomeInvalido(String nomeInvalido) {
            assertThatThrownBy(() -> TipoUsuario.restaurar(ID, restauranteValido(), nomeInvalido, TIPO_VALIDO))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Nome inválido");
        }

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando o tipo de usuário for nulo")
        void deveLancarExcecao_quandoTipoNulo() {
            assertThatThrownBy(() -> TipoUsuario.restaurar(ID, restauranteValido(), NOME_VALIDO, null))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Tipo de usuário inválido");
        }
    }

    // -------------------------------------------------------------------------
    // TipoUsuario.atualizar
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("atualizar()")
    class Atualizar {

        @Test
        @DisplayName("Deve atualizar o nome com sucesso quando o novo nome é válido")
        void deveAtualizarNome_quandoNovoNomeValido() {
            TipoUsuario tipo = TipoUsuario.restaurar(ID, restauranteValido(), NOME_VALIDO, TIPO_VALIDO);
            String novoNome = "Supervisor";

            tipo.atualizar(novoNome);

            assertThat(tipo.getNome()).isEqualTo(novoNome);
        }

        @Test
        @DisplayName("Deve manter o tipo (enum) inalterado após atualização do nome")
        void deveManterTipo_aposAtualizarNome() {
            TipoUsuario tipo = TipoUsuario.restaurar(ID, restauranteValido(), NOME_VALIDO, TIPO_VALIDO);

            tipo.atualizar("Supervisor");

            assertThat(tipo.getCategoria()).isEqualTo(TIPO_VALIDO);
        }

        @ParameterizedTest(name = "novoNome = \"{0}\"")
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Deve lançar DadosInvalidosException quando o novo nome for nulo, vazio ou em branco")
        void deveLancarExcecao_quandoNovoNomeInvalido(String nomeInvalido) {
            TipoUsuario tipo = TipoUsuario.restaurar(ID, restauranteValido(), NOME_VALIDO, TIPO_VALIDO);

            assertThatThrownBy(() -> tipo.atualizar(nomeInvalido))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Nome inválido");
        }
    }

    // -------------------------------------------------------------------------
    // Getters auxiliares
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("getIdRestaurante()")
    class GetIdRestaurante {

        @Test
        @DisplayName("Deve retornar o id do restaurante quando o restaurante está presente")
        void deveRetornarIdRestaurante_quandoRestaurantePresente() {
            Restaurante restaurante = Restaurante.restaurar(10L, "Restaurante Teste",null,null,null,null,null);
            TipoUsuario tipo = TipoUsuario.criar(restaurante, NOME_VALIDO, TIPO_VALIDO);

            assertThat(tipo.getIdRestaurante()).isEqualTo(10L);
        }
    }
}
