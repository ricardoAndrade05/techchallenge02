package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import com.postech.techchallenge02.core.dtos.ExcluirRestauranteDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExcluirRestauranteUseCase")
class ExcluirRestauranteUseCaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private ExcluirRestauranteUseCase useCase;

    private static final Long ID_RESTAURANTE = 1L;
    private static final Long ID_DONO = 10L;
    private static final Long ID_OUTRO_USUARIO = 99L;

    private Usuario dono;
    private Restaurante restaurante;
    private ExcluirRestauranteDTO dto;

    @BeforeEach
    void setUp() {
        useCase = ExcluirRestauranteUseCase.getInstance(restauranteGateway, usuarioGateway);

        Endereco endereco = Endereco.restaurar(100L, "Rua Central", "10", "SP", "SP", "01000-000");
        restaurante = Restaurante.restaurar(ID_RESTAURANTE, "Restaurante", TipoCozinhaEnun.BRASILEIRA,
                LocalTime.of(10, 0), LocalTime.of(22, 0), endereco, null);
        TipoUsuario tipoDono = TipoUsuario.restaurar(500L, restaurante, "Proprietário", CategoriaEnun.DONO);
        dono = Usuario.restaurar(ID_DONO, "Dono", tipoDono);
        restaurante.atualizar(null, null, null, null, dono);

        dto = new ExcluirRestauranteDTO(ID_DONO, ID_RESTAURANTE);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o usuário não for encontrado")
    void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(restauranteGateway, never()).excluirRestaurante(any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o restaurante não for encontrado")
    void deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Restaurante não encontrado");

        verify(restauranteGateway, never()).excluirRestaurante(any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando usuário não tiver permissão para excluir")
    void deveLancarExcecao_quandoUsuarioNaoTiverPermissaoDeExcluir() {
        TipoUsuario tipoCliente = TipoUsuario.restaurar(501L, restaurante, "Cliente", CategoriaEnun.CLIENTE);
        Usuario usuarioCliente = Usuario.restaurar(ID_DONO, "Cliente", tipoCliente);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioCliente));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("Usuário não tem permissão para excluir restaurante");

        verify(restauranteGateway, never()).excluirRestaurante(any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando o usuário não for o dono do restaurante")
    void deveLancarExcecao_quandoUsuarioNaoForDonoDoRestaurante() {
        TipoUsuario tipoDonoOutro = TipoUsuario.restaurar(502L, restaurante, "Dono Outro", CategoriaEnun.DONO);
        Usuario outroUsuario = Usuario.restaurar(ID_OUTRO_USUARIO, "Outro Usuário", tipoDonoOutro);
        ExcluirRestauranteDTO dtoOutro = new ExcluirRestauranteDTO(ID_OUTRO_USUARIO, ID_RESTAURANTE);

        when(usuarioGateway.buscaUsuarioPorId(ID_OUTRO_USUARIO, ID_RESTAURANTE)).thenReturn(Optional.of(outroUsuario));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));

        assertThatThrownBy(() -> useCase.execute(dtoOutro))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("Usuário não tem permissão para excluir restaurante");

        verify(restauranteGateway, never()).excluirRestaurante(any());
    }

    @Test
    @DisplayName("execute - Deve excluir restaurante com sucesso quando usuário for o dono")
    void deveExcluirRestauranteComSucesso() {
        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));

        assertThatCode(() -> useCase.execute(dto)).doesNotThrowAnyException();

        verify(restauranteGateway).excluirRestaurante(ID_RESTAURANTE);
    }
}
