package com.postech.techchallenge02.core.presenters;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;

@DisplayName("RestaurantePresenter")
class RestaurantePresenterTest {

    private static final Long ID_RESTAURANTE = 1L;
    private static final String NOME_RESTAURANTE = "Restaurante Sabor & Arte";
    private static final TipoCozinhaEnun TIPO_COZINHA = TipoCozinhaEnun.BRASILEIRA;
    private static final LocalTime ABERTURA = LocalTime.of(11, 30);
    private static final LocalTime FECHAMENTO = LocalTime.of(22, 30);

    private Endereco endereco;
    private Usuario dono;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        endereco = Endereco.restaurar(10L, "Av Paulista", "1000", "São Paulo", "SP", "01310-100");
        TipoUsuario tipoDono = TipoUsuario.restaurar(100L, null, "Proprietário", CategoriaEnun.DONO);
        dono = Usuario.restaurar(50L, "Mario Rossi", tipoDono);
        restaurante = Restaurante.restaurar(ID_RESTAURANTE, NOME_RESTAURANTE, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, dono);
    }

    @Test
    @DisplayName("toDTO - Deve converter Restaurante para RestauranteDTO com todos os campos preenchidos")
    void deveConverterParaDTO_comTodosOsCamposPreenchidos() {
        RestauranteDTO dto = RestaurantePresenter.toDTO(restaurante);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(ID_RESTAURANTE);
        assertThat(dto.nome()).isEqualTo(NOME_RESTAURANTE);
        assertThat(dto.tipoCozinha()).isEqualTo(TIPO_COZINHA);
        assertThat(dto.horarioInicio()).isEqualTo(ABERTURA);
        assertThat(dto.horarioFim()).isEqualTo(FECHAMENTO);

        assertThat(dto.endereco()).isNotNull();
        assertThat(dto.endereco().id()).isEqualTo(10L);
        assertThat(dto.endereco().logradouro()).isEqualTo("Av Paulista");
        assertThat(dto.endereco().numero()).isEqualTo("1000");
        assertThat(dto.endereco().cidade()).isEqualTo("São Paulo");
        assertThat(dto.endereco().estado()).isEqualTo("SP");
        assertThat(dto.endereco().cep()).isEqualTo("01310-100");

        assertThat(dto.dono()).isNotNull();
        assertThat(dto.dono().id()).isEqualTo(50L);
        assertThat(dto.dono().nome()).isEqualTo("Mario Rossi");
        assertThat(dto.dono().tipoUsuario()).isNotNull();
        assertThat(dto.dono().tipoUsuario().id()).isEqualTo(100L);
        assertThat(dto.dono().tipoUsuario().nome()).isEqualTo("Proprietário");
        assertThat(dto.dono().tipoUsuario().tipo()).isEqualTo(CategoriaEnun.DONO);
    }

    @Test
    @DisplayName("toDTO - Deve retornar null quando restaurante for nulo")
    void deveRetornarNull_quandoRestauranteNulo() {
        RestauranteDTO dto = RestaurantePresenter.toDTO(null);

        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("toDTO - Deve mapear endereço como null quando endereco do restaurante for nulo")
    void deveMapearEnderecoComoNull_quandoEnderecoForNulo() {
        Restaurante restauranteSemEndereco = Restaurante.restaurar(ID_RESTAURANTE, NOME_RESTAURANTE, TIPO_COZINHA, ABERTURA, FECHAMENTO, null, dono);

        RestauranteDTO dto = RestaurantePresenter.toDTO(restauranteSemEndereco);

        assertThat(dto).isNotNull();
        assertThat(dto.endereco()).isNull();
    }

    @Test
    @DisplayName("toDTO - Deve mapear dono como null quando dono do restaurante for nulo")
    void deveMapearDonoComoNull_quandoDonoForNulo() {
        Restaurante restauranteSemDono = Restaurante.restaurar(ID_RESTAURANTE, NOME_RESTAURANTE, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, null);

        RestauranteDTO dto = RestaurantePresenter.toDTO(restauranteSemDono);

        assertThat(dto).isNotNull();
        assertThat(dto.dono()).isNull();
    }

    @Test
    @DisplayName("toDTO - Deve mapear tipoUsuarioDonoDTO como null quando dono não tiver tipo de usuário")
    void deveMapearTipoUsuarioComoNull_quandoDonoSemTipoUsuario() {
        Usuario donoSemTipo = Usuario.restaurar(50L, "Mario Rossi", null);
        Restaurante rest = Restaurante.restaurar(ID_RESTAURANTE, NOME_RESTAURANTE, TIPO_COZINHA, ABERTURA, FECHAMENTO, endereco, donoSemTipo);

        RestauranteDTO dto = RestaurantePresenter.toDTO(rest);

        assertThat(dto).isNotNull();
        assertThat(dto.dono()).isNotNull();
        assertThat(dto.dono().tipoUsuario()).isNull();
    }
}
