package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarTipoUsuarioPorIdUseCase")
class BuscarTipoUsuarioPorIdUseCaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    private BuscarTipoUsuarioPorIdUseCase useCase;

    private static final Long ID_TIPO_USUARIO_VALIDO = 1L;
    private static final Long ID_TIPO_USUARIO_INEXISTENTE = 999L;

    @BeforeEach
    void setUp() {
        useCase = BuscarTipoUsuarioPorIdUseCase.getInstance(tipoUsuarioGateway);
    }

    @Nested
    @DisplayName("Dado que o id fornecido é inválido")
    class QuandoIdInvalido {

        @Test
        @DisplayName("Deve lançar DadosInvalidosException quando o id for nulo")
        void deveLancarExcecao_quandoIdNulo() {
            assertThatThrownBy(() -> useCase.execute(null))
                    .isInstanceOf(DadosInvalidosException.class)
                    .hasMessageContaining("Tipo de usuário inválido");

            verify(tipoUsuarioGateway, never()).buscaTipoUsuarioPorId(null);
        }
    }

    @Nested
    @DisplayName("Dado que o tipo de usuário não existe")
    class QuandoTipoUsuarioNaoExiste {

        @Test
        @DisplayName("Deve lançar TipoUsuarioNaoEncontradoException quando o tipo não for encontrado")
        void deveLancarExcecao_quandoTipoUsuarioNaoEncontrado() {
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO_INEXISTENTE)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(ID_TIPO_USUARIO_INEXISTENTE))
                    .isInstanceOf(RecursoNaoEncontradoException.class)
                    .hasMessageContaining(String.valueOf(ID_TIPO_USUARIO_INEXISTENTE));
        }
    }

    @Nested
    @DisplayName("Dado que o tipo de usuário existe")
    class QuandoTipoUsuarioExiste {

        @Test
        @DisplayName("Deve retornar o tipo de usuário quando o id for válido e o tipo existir")
        void deveRetornarTipoUsuario_quandoIdValidoETipoExiste() {
            Restaurante restaurante = Restaurante.criar(10L, "Restaurante Teste");
            TipoUsuario tipoEsperado = TipoUsuario.restaurar(ID_TIPO_USUARIO_VALIDO, restaurante, "Gerente", TipoUsuarioEnun.DONO);
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO_VALIDO)).thenReturn(Optional.of(tipoEsperado));

            TipoUsuario resultado = useCase.execute(ID_TIPO_USUARIO_VALIDO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(ID_TIPO_USUARIO_VALIDO);
            assertThat(resultado.getNome()).isEqualTo("Gerente");
            assertThat(resultado.getTipo()).isEqualTo(TipoUsuarioEnun.DONO);
        }

        @Test
        @DisplayName("Deve invocar o gateway para buscar o tipo de usuário pelo id")
        void deveInvocarGateway_quandoBuscarPorId() {
            Restaurante restaurante = Restaurante.criar(10L, "Restaurante Teste");
            TipoUsuario tipo = TipoUsuario.restaurar(ID_TIPO_USUARIO_VALIDO, restaurante, "Gerente", TipoUsuarioEnun.DONO);
            when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO_VALIDO)).thenReturn(Optional.of(tipo));

            useCase.execute(ID_TIPO_USUARIO_VALIDO);

            verify(tipoUsuarioGateway).buscaTipoUsuarioPorId(ID_TIPO_USUARIO_VALIDO);
        }
    }
}
