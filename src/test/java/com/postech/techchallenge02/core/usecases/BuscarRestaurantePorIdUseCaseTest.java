package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarRestaurantePorIdUseCase")
class BuscarRestaurantePorIdUseCaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    private BuscarRestaurantePorIdUseCase useCase;

    private static final Long ID_VALIDO = 1L;
    private static final Long ID_INEXISTENTE = 999L;

    @BeforeEach
    void setUp() {
        useCase = BuscarRestaurantePorIdUseCase.getInstance(restauranteGateway);
    }

    @Test
    @DisplayName("execute - Deve lançar DadosInvalidosException quando o id do restaurante for nulo")
    void deveLancarExcecao_quandoIdNulo() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Restaurante inválido.");

        verify(restauranteGateway, never()).buscaRestaurantePorId(null);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o restaurante não for encontrado")
    void deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        when(restauranteGateway.buscaRestaurantePorId(ID_INEXISTENTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(ID_INEXISTENTE))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Não há restaurante com id: " + ID_INEXISTENTE);
    }

    @Test
    @DisplayName("execute - Deve retornar o restaurante quando encontrado com sucesso")
    void deveRetornarRestaurante_quandoEncontrado() {
        Endereco endereco = Endereco.restaurar(10L, "Av Paulista", "100", "São Paulo", "SP", "01310-000");
        TipoUsuario tipoDono = TipoUsuario.restaurar(100L, null, "Proprietário", CategoriaEnun.DONO);
        Usuario dono = Usuario.restaurar(50L, "Dono", tipoDono);
        Restaurante restaurante = Restaurante.restaurar(ID_VALIDO, "Bistrô", TipoCozinhaEnun.ITALIANA, LocalTime.of(12, 0), LocalTime.of(22, 0), endereco, dono);

        when(restauranteGateway.buscaRestaurantePorId(ID_VALIDO)).thenReturn(Optional.of(restaurante));

        Restaurante resultado = useCase.execute(ID_VALIDO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(ID_VALIDO);
        assertThat(resultado.getNome()).isEqualTo("Bistrô");
        verify(restauranteGateway).buscaRestaurantePorId(ID_VALIDO);
    }
}
