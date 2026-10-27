package com.postech.techchallenge02.core.entities;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

@DisplayName("Endereco")
class EnderecoTest {

    private static final Long ID = 1L;
    private static final String LOGRADOURO_VALIDO = "Avenida Paulista";
    private static final String NUMERO_VALIDO = "1000";
    private static final String CIDADE_VALIDA = "São Paulo";
    private static final String ESTADO_VALIDO = "SP";
    private static final String CEP_VALIDO = "01310-100";

    @Test
    @DisplayName("criar - Deve criar uma instância com id nulo quando os dados são válidos")
    void deveCriarComIdNulo_quandoDadosValidos() {
        Endereco endereco = Endereco.criar(LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThat(endereco.getId()).isNull();
        assertThat(endereco.getLogradouro()).isEqualTo(LOGRADOURO_VALIDO);
        assertThat(endereco.getNumero()).isEqualTo(NUMERO_VALIDO);
        assertThat(endereco.getCidade()).isEqualTo(CIDADE_VALIDA);
        assertThat(endereco.getEstado()).isEqualTo(ESTADO_VALIDO);
        assertThat(endereco.getCep()).isEqualTo(CEP_VALIDO);
    }

    @ParameterizedTest(name = "logradouro = \"{0}\"")
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o logradouro for vazio ou em branco")
    void deveLancarExcecao_quandoLogradouroEmBranco(String logradouroInvalido) {
        assertThatThrownBy(() -> Endereco.criar(logradouroInvalido, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O logradouro não pode ser nulo ou vazio");
    }

    @ParameterizedTest(name = "numero = \"{0}\"")
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o número for vazio ou em branco")
    void deveLancarExcecao_quandoNumeroEmBranco(String numeroInvalido) {
        assertThatThrownBy(() -> Endereco.criar(LOGRADOURO_VALIDO, numeroInvalido, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O número não pode ser nulo ou vazio");
    }

    @ParameterizedTest(name = "cidade = \"{0}\"")
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("criar - Deve lançar DadosInvalidosException quando a cidade for vazia ou em branco")
    void deveLancarExcecao_quandoCidadeEmBranco(String cidadeInvalida) {
        assertThatThrownBy(() -> Endereco.criar(LOGRADOURO_VALIDO, NUMERO_VALIDO, cidadeInvalida, ESTADO_VALIDO, CEP_VALIDO))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("A cidade não pode ser nula ou vazia");
    }

    @ParameterizedTest(name = "estado = \"{0}\"")
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o estado for vazio ou em branco")
    void deveLancarExcecao_quandoEstadoEmBranco(String estadoInvalido) {
        assertThatThrownBy(() -> Endereco.criar(LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, estadoInvalido, CEP_VALIDO))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O estado não pode ser nulo ou vazio");
    }

    @ParameterizedTest(name = "cep = \"{0}\"")
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o cep for vazio ou em branco")
    void deveLancarExcecao_quandoCepEmBranco(String cepInvalido) {
        assertThatThrownBy(() -> Endereco.criar(LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, cepInvalido))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O cep não pode ser nulo ou vazio");
    }

    @Test
    @DisplayName("restaurar - Deve restaurar instância completa com id quando os dados são fornecidos")
    void deveRestaurarInstancia_comTodosOsCamposEId() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThat(endereco.getId()).isEqualTo(ID);
        assertThat(endereco.getLogradouro()).isEqualTo(LOGRADOURO_VALIDO);
        assertThat(endereco.getNumero()).isEqualTo(NUMERO_VALIDO);
        assertThat(endereco.getCidade()).isEqualTo(CIDADE_VALIDA);
        assertThat(endereco.getEstado()).isEqualTo(ESTADO_VALIDO);
        assertThat(endereco.getCep()).isEqualTo(CEP_VALIDO);
    }

    @Test
    @DisplayName("atualizar - Deve atualizar todos os campos quando todos os novos valores forem válidos")
    void deveAtualizarTodosOsCampos_quandoNovosValoresForemValidos() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        endereco.atualizar("Rua Augusta", "500", "Campinas", "SP", "13010-000");

        assertThat(endereco.getLogradouro()).isEqualTo("Rua Augusta");
        assertThat(endereco.getNumero()).isEqualTo("500");
        assertThat(endereco.getCidade()).isEqualTo("Campinas");
        assertThat(endereco.getEstado()).isEqualTo("SP");
        assertThat(endereco.getCep()).isEqualTo("13010-000");
    }

    @Test
    @DisplayName("atualizar - Deve manter campos inalterados quando parâmetros forem nulos ou em branco")
    void deveManterCamposInalterados_quandoParametrosForemNulosOuEmBranco() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        endereco.atualizar(null, "   ", null, "", "   ");

        assertThat(endereco.getLogradouro()).isEqualTo(LOGRADOURO_VALIDO);
        assertThat(endereco.getNumero()).isEqualTo(NUMERO_VALIDO);
        assertThat(endereco.getCidade()).isEqualTo(CIDADE_VALIDA);
        assertThat(endereco.getEstado()).isEqualTo(ESTADO_VALIDO);
        assertThat(endereco.getCep()).isEqualTo(CEP_VALIDO);
    }

    @Test
    @DisplayName("atualizar - Deve atualizar apenas os campos informados e válidos")
    void deveAtualizarApenasCamposInformados() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        endereco.atualizar("Nova Rua", null, null, null, null);

        assertThat(endereco.getLogradouro()).isEqualTo("Nova Rua");
        assertThat(endereco.getNumero()).isEqualTo(NUMERO_VALIDO);
        assertThat(endereco.getCidade()).isEqualTo(CIDADE_VALIDA);
        assertThat(endereco.getEstado()).isEqualTo(ESTADO_VALIDO);
        assertThat(endereco.getCep()).isEqualTo(CEP_VALIDO);
    }

    @Test
    @DisplayName("setters - Deve atualizar valores individuais corretamente")
    void deveAtualizarValoresIndividuaisPorSetters() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        endereco.setLogradouro("Rua Nova");
        endereco.setNumero("123");
        endereco.setCidade("Curitiba");
        endereco.setEstado("PR");
        endereco.setCep("80000-000");

        assertThat(endereco.getLogradouro()).isEqualTo("Rua Nova");
        assertThat(endereco.getNumero()).isEqualTo("123");
        assertThat(endereco.getCidade()).isEqualTo("Curitiba");
        assertThat(endereco.getEstado()).isEqualTo("PR");
        assertThat(endereco.getCep()).isEqualTo("80000-000");
    }

    @Test
    @DisplayName("setLogradouro - Deve lançar DadosInvalidosException quando valor for em branco")
    void deveLancarExcecao_quandoSetLogradouroEmBranco() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThatThrownBy(() -> endereco.setLogradouro("  "))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O logradouro não pode ser nulo ou vazio");
    }

    @Test
    @DisplayName("setNumero - Deve lançar DadosInvalidosException quando valor for em branco")
    void deveLancarExcecao_quandoSetNumeroEmBranco() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThatThrownBy(() -> endereco.setNumero("  "))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O número não pode ser nulo ou vazio");
    }

    @Test
    @DisplayName("setCidade - Deve lançar DadosInvalidosException quando valor for em branco")
    void deveLancarExcecao_quandoSetCidadeEmBranco() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThatThrownBy(() -> endereco.setCidade("  "))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("A cidade não pode ser nula ou vazia");
    }

    @Test
    @DisplayName("setEstado - Deve lançar DadosInvalidosException quando valor for em branco")
    void deveLancarExcecao_quandoSetEstadoEmBranco() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThatThrownBy(() -> endereco.setEstado("  "))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O estado não pode ser nulo ou vazio");
    }

    @Test
    @DisplayName("setCep - Deve lançar DadosInvalidosException quando valor for em branco")
    void deveLancarExcecao_quandoSetCepEmBranco() {
        Endereco endereco = Endereco.restaurar(ID, LOGRADOURO_VALIDO, NUMERO_VALIDO, CIDADE_VALIDA, ESTADO_VALIDO, CEP_VALIDO);

        assertThatThrownBy(() -> endereco.setCep("  "))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O cep não pode ser nulo ou vazio");
    }
}
