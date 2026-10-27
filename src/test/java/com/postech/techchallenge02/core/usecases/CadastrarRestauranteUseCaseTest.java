package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarRestauranteUseCase")
class CadastrarRestauranteUseCaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    private CadastrarRestauranteUseCase useCase;

    private static final Long ID_USUARIO = 1L;
    private static final Long ID_RESTAURANTE_SALVO = 10L;
    private static final Long ID_TIPO_SALVO = 100L;

    private EnderecoDTO enderecoDTO;
    private NovoRestauranteDTO novoRestauranteDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        useCase = CadastrarRestauranteUseCase.getInstance(restauranteGateway, usuarioGateway, tipoUsuarioGateway);

        enderecoDTO = new EnderecoDTO(null, "Av Brasil", "500", "Rio de Janeiro", "RJ", "20000-000");
        novoRestauranteDTO = new NovoRestauranteDTO(
                "Restaurante Novo", enderecoDTO, 1,
                LocalTime.of(10, 0), LocalTime.of(22, 0), ID_USUARIO);

        usuario = Usuario.restaurar(ID_USUARIO, "João Dono", null);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando usuário logado não for encontrado")
    void deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(novoRestauranteDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(restauranteGateway, never()).cadastrar(any());
        verify(tipoUsuarioGateway, never()).cadastraTipoUsuario(any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando falhar ao recuperar o restaurante cadastrado")
    void deveLancarExcecao_quandoFalharAoRecuperarRestauranteCadastrado() {
        Endereco enderecoCriado = Endereco.restaurar(1L, "Av Brasil", "500", "Rio de Janeiro", "RJ", "20000-000");
        Restaurante restauranteCadastrado = Restaurante.restaurar(
                ID_RESTAURANTE_SALVO, "Restaurante Novo", TipoCozinhaEnun.BRASILEIRA,
                LocalTime.of(10, 0), LocalTime.of(22, 0), enderecoCriado, usuario);
        TipoUsuario tipoCadastrado = TipoUsuario.restaurar(ID_TIPO_SALVO, restauranteCadastrado, "Proprietário", CategoriaEnun.DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO)).thenReturn(Optional.of(usuario));
        when(restauranteGateway.cadastrar(any(Restaurante.class))).thenReturn(restauranteCadastrado);
        when(tipoUsuarioGateway.cadastraTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoCadastrado);
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE_SALVO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(novoRestauranteDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Erro ao recuperar o restaurante cadastrado");

        verify(usuarioGateway).vincularUsuarioAoTipo(ID_USUARIO, ID_TIPO_SALVO);
    }

    @Test
    @DisplayName("execute - Deve cadastrar restaurante, tipo proprietário e vincular usuário com sucesso")
    void deveCadastrarRestauranteComSucesso() {
        Endereco enderecoCriado = Endereco.restaurar(1L, "Av Brasil", "500", "Rio de Janeiro", "RJ", "20000-000");
        Restaurante restauranteCadastrado = Restaurante.restaurar(
                ID_RESTAURANTE_SALVO, "Restaurante Novo", TipoCozinhaEnun.BRASILEIRA,
                LocalTime.of(10, 0), LocalTime.of(22, 0), enderecoCriado, usuario);
        TipoUsuario tipoCadastrado = TipoUsuario.restaurar(ID_TIPO_SALVO, restauranteCadastrado, "Proprietário", CategoriaEnun.DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO)).thenReturn(Optional.of(usuario));
        when(restauranteGateway.cadastrar(any(Restaurante.class))).thenReturn(restauranteCadastrado);
        when(tipoUsuarioGateway.cadastraTipoUsuario(any(TipoUsuario.class))).thenReturn(tipoCadastrado);
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE_SALVO)).thenReturn(Optional.of(restauranteCadastrado));

        Restaurante resultado = useCase.execute(novoRestauranteDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(ID_RESTAURANTE_SALVO);
        assertThat(resultado.getNome()).isEqualTo("Restaurante Novo");

        verify(restauranteGateway).cadastrar(any(Restaurante.class));
        verify(tipoUsuarioGateway).cadastraTipoUsuario(any(TipoUsuario.class));
        verify(usuarioGateway).vincularUsuarioAoTipo(ID_USUARIO, ID_TIPO_SALVO);
        verify(restauranteGateway).buscaRestaurantePorId(ID_RESTAURANTE_SALVO);
    }
}
