package com.postech.techchallenge02.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

@DisplayName("Restaurante")
class RestauranteTest {

    private static final Long ID = 1L;
    private static final String NOME_VALIDO = "Cantina Italiana";
    private static final TipoCozinhaEnun TIPO_COZINHA = TipoCozinhaEnun.ITALIANA;
    private static final LocalTime ABERTURA = LocalTime.of(11, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(23, 0);

    private Endereco endereco;
    private Usuario dono;

    @BeforeEach
    void setUp() {
        endereco = Endereco.restaurar(10L, "Rua das Flores", "123", "São Paulo", "SP", "01001-000");
        TipoUsuario tipoDono = TipoUsuario.restaurar(100L, null, "Proprietário", CategoriaEnun.DONO);
        dono = Usuario.restaurar(50L, "Mario Rossi", tipoDono);
    }

    @Test
    @DisplayName("criar - Deve criar restaurante com id nulo quando os dados são válidos")
    void deveCriarComIdNulo_quandoDadosValidos() {
        Restaurante restaurante = Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);

        assertThat(restaurante.getId()).isNull();
        assertThat(restaurante.getNome()).isEqualTo(NOME_VALIDO);
        assertThat(restaurante.getTipoCozinha()).isEqualTo(TIPO_COZINHA);
        assertThat(restaurante.getHorarioAbertura()).isEqualTo(ABERTURA);
        assertThat(restaurante.getHorarioFechamento()).isEqualTo(FECHAMENTO);
        assertThat(restaurante.getEndereco()).isEqualTo(endereco);
        assertThat(restaurante.getDono()).isEqualTo(dono);
    }

    @Test
    @DisplayName("criar - Deve remover espaços no início e final do nome")
    void deveTrimarNome_quandoCriar() {
        Restaurante restaurante = Restaurante.criar("  Cantina com Espaços  ", TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);

        assertThat(restaurante.getNome()).isEqualTo("Cantina com Espaços");
    }

    @ParameterizedTest(name = "nome = \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o nome for nulo, vazio ou em branco")
    void deveLancarExcecao_quandoNomeInvalido(String nomeInvalido) {
        assertThatThrownBy(() -> Restaurante.criar(nomeInvalido, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O nome não pode ser nulo ou vazio");
    }

    @Test
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o tipo de cozinha for nulo")
    void deveLancarExcecao_quandoTipoCozinhaNulo() {
        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, null, ABERTURA, FECHAMENTO, endereco, dono))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O tipo de cozinha não pode ser nulo");
    }

    @Test
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o horário de abertura for nulo")
    void deveLancarExcecao_quandoHorarioAberturaNulo() {
        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, null, FECHAMENTO, endereco, dono))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O horário de abertura não pode ser nulo.");
    }

    @Test
    @DisplayName("criar - Deve lançar DadosInvalidosException quando o horário de fechamento for nulo")
    void deveLancarExcecao_quandoHorarioFechamentoNulo() {
        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, ABERTURA, null, endereco, dono))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O horário de fechamento não pode ser nulo.");
    }

    @Test
    @DisplayName("criar - Deve lançar DadosInvalidosException quando abertura for após fechamento")
    void deveLancarExcecao_quandoHorarioAberturaAposFechamento() {
        LocalTime aberturaTardia = LocalTime.of(23, 0);
        LocalTime fechamentoCedo = LocalTime.of(11, 0);

        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, aberturaTardia, fechamentoCedo, endereco, dono))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O horário de abertura deve ser anterior que o horário de fechamento.");
    }

    @Test
    @DisplayName("criar - Deve lançar DadosInvalidosException quando abertura for igual ao fechamento")
    void deveLancarExcecao_quandoHorariosIguais() {
        LocalTime mesmoHorario = LocalTime.of(12, 0);

        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, mesmoHorario, mesmoHorario, endereco, dono))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Os horarios de abertura/fechamento devem ser diferentes.");
    }

    @Test
    @DisplayName("criar - Deve lançar IllegalArgumentException quando o endereço for nulo")
    void deveLancarExcecao_quandoEnderecoNulo() {
        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, null, dono))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("O endereço não pode ser nulo");
    }

    @Test
    @DisplayName("criar - Deve lançar IllegalArgumentException quando o dono for nulo")
    void deveLancarExcecao_quandoDonoNulo() {
        assertThatThrownBy(() -> Restaurante.criar(NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("O dono não pode ser nulo");
    }

    @Test
    @DisplayName("restaurar - Deve restaurar instância completa com id e dados fornecidos")
    void deveRestaurarInstancia_comTodosOsCamposEId() {
        Restaurante restaurante = Restaurante.restaurar(ID, NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);

        assertThat(restaurante.getId()).isEqualTo(ID);
        assertThat(restaurante.getNome()).isEqualTo(NOME_VALIDO);
        assertThat(restaurante.getTipoCozinha()).isEqualTo(TIPO_COZINHA);
        assertThat(restaurante.getHorarioAbertura()).isEqualTo(ABERTURA);
        assertThat(restaurante.getHorarioFechamento()).isEqualTo(FECHAMENTO);
        assertThat(restaurante.getEndereco()).isEqualTo(endereco);
        assertThat(restaurante.getDono()).isEqualTo(dono);
    }

    @Test
    @DisplayName("atualizar - Deve atualizar todos os campos quando novos dados são válidos")
    void deveAtualizarTodosOsCampos_quandoDadosValidos() {
        Restaurante restaurante = Restaurante.restaurar(ID, NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);
        Usuario novoDono = Usuario.restaurar(60L, "Luigi Rossi", null);
        LocalTime novaAbertura = LocalTime.of(10, 0);
        LocalTime novoFechamento = LocalTime.of(22, 0);

        restaurante.atualizar("Novo Nome Tratoria", TipoCozinhaEnun.BRASILEIRA, novaAbertura, novoFechamento, novoDono);

        assertThat(restaurante.getNome()).isEqualTo("Novo Nome Tratoria");
        assertThat(restaurante.getTipoCozinha()).isEqualTo(TipoCozinhaEnun.BRASILEIRA);
        assertThat(restaurante.getHorarioAbertura()).isEqualTo(novaAbertura);
        assertThat(restaurante.getHorarioFechamento()).isEqualTo(novoFechamento);
        assertThat(restaurante.getDono()).isEqualTo(novoDono);
    }

    @Test
    @DisplayName("atualizar - Deve manter valores existentes quando novos parâmetros forem nulos ou vazios")
    void deveManterValoresExistentes_quandoParametrosForemNulosOuVazios() {
        Restaurante restaurante = Restaurante.restaurar(ID, NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);

        restaurante.atualizar(null, null, null, null, null);

        assertThat(restaurante.getNome()).isEqualTo(NOME_VALIDO);
        assertThat(restaurante.getTipoCozinha()).isEqualTo(TIPO_COZINHA);
        assertThat(restaurante.getHorarioAbertura()).isEqualTo(ABERTURA);
        assertThat(restaurante.getHorarioFechamento()).isEqualTo(FECHAMENTO);
        assertThat(restaurante.getDono()).isEqualTo(dono);
    }

    @Test
    @DisplayName("atualizar - Deve manter nome original quando o novo nome for vazio ou em branco")
    void deveManterNomeOriginal_quandoNovoNomeForEmBranco() {
        Restaurante restaurante = Restaurante.restaurar(ID, NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);

        restaurante.atualizar("   ", null, null, null, null);

        assertThat(restaurante.getNome()).isEqualTo(NOME_VALIDO);
    }

    @Test
    @DisplayName("atualizar - Deve validar novos horários e lançar DadosInvalidosException se abertura for posterior ao fechamento existente")
    void deveLancarExcecao_quandoNovaAberturaForInvalidaComFechamentoExistente() {
        Restaurante restaurante = Restaurante.restaurar(ID, NOME_VALIDO, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);
        LocalTime aberturaAposFechamento = LocalTime.of(23, 30);

        assertThatThrownBy(() -> restaurante.atualizar(null, null, aberturaAposFechamento, null, null))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("O horário de abertura deve ser anterior que o horário de fechamento.");
    }
}
