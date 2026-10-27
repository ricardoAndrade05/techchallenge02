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

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.EnderecoDTO;
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
@DisplayName("AtualizarRestauranteUseCase")
class AtualizarRestauranteUseCaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private AtualizarRestauranteUseCase useCase;

    private static final Long ID_RESTAURANTE = 1L;
    private static final Long ID_DONO = 10L;
    private static final Long ID_NOVO_DONO = 20L;
    private static final Long ID_OUTRO_USUARIO = 99L;

    private Endereco endereco;
    private TipoUsuario tipoDono;
    private Usuario dono;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        useCase = AtualizarRestauranteUseCase.getInstance(restauranteGateway, usuarioGateway);

        endereco = Endereco.restaurar(100L, "Rua Antiga", "10", "SP", "SP", "01000-000");
        restaurante = Restaurante.restaurar(ID_RESTAURANTE, "Restaurante Original", TipoCozinhaEnun.BRASILEIRA,
                LocalTime.of(11, 0), LocalTime.of(22, 0), endereco, null);
        tipoDono = TipoUsuario.restaurar(500L, restaurante, "Proprietário", CategoriaEnun.DONO);
        dono = Usuario.restaurar(ID_DONO, "Dono Atual", tipoDono);
        restaurante.atualizar(null, null, null, null, dono);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando usuário logado não for encontrado")
    void deveLancarExcecao_quandoUsuarioLogadoNaoEncontrado() {
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(ID_RESTAURANTE, "Novo Nome", null, null, null, null, null, ID_DONO);
        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuário logado não encontrado");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando restaurante não for encontrado")
    void deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(ID_RESTAURANTE, "Novo Nome", null, null, null, null, null, ID_DONO);
        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Restaurante não encontrado");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando usuário logado não puder atualizar")
    void deveLancarExcecao_quandoUsuarioSemPermissaoDeAtualizar() {
        TipoUsuario tipoCliente = TipoUsuario.restaurar(501L, restaurante, "Cliente", CategoriaEnun.CLIENTE);
        Usuario usuarioCliente = Usuario.restaurar(ID_DONO, "Cliente", tipoCliente);
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(ID_RESTAURANTE, "Novo Nome", null, null, null, null, null, ID_DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioCliente));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("Usuário não tem permissão");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando usuário logado não for o dono")
    void deveLancarExcecao_quandoUsuarioNaoForODono() {
        Usuario outroUsuario = Usuario.restaurar(ID_OUTRO_USUARIO, "Outro", tipoDono);
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(ID_RESTAURANTE, "Novo Nome", null, null, null, null, null, ID_OUTRO_USUARIO);

        when(usuarioGateway.buscaUsuarioPorId(ID_OUTRO_USUARIO, ID_RESTAURANTE)).thenReturn(Optional.of(outroUsuario));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("Usuário não tem permissão");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("execute - Deve atualizar dados básicos e endereço do restaurante com sucesso")
    void deveAtualizarDadosBasicosEEnderecoComSucesso() {
        EnderecoDTO novoEnderecoDto = new EnderecoDTO(null, "Rua Nova", "200", "Campinas", "SP", "13000-000");
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                ID_RESTAURANTE, "Restaurante Atualizado", novoEnderecoDto,
                TipoCozinhaEnun.ITALIANA, LocalTime.of(12, 0), LocalTime.of(23, 0),
                null, ID_DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
        when(restauranteGateway.atualizar(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Restaurante atualizado = useCase.execute(dto);

        assertThat(atualizado).isNotNull();
        assertThat(atualizado.getNome()).isEqualTo("Restaurante Atualizado");
        assertThat(atualizado.getTipoCozinha()).isEqualTo(TipoCozinhaEnun.ITALIANA);
        assertThat(atualizado.getHorarioAbertura()).isEqualTo(LocalTime.of(12, 0));
        assertThat(atualizado.getHorarioFechamento()).isEqualTo(LocalTime.of(23, 0));
        assertThat(atualizado.getEndereco().getLogradouro()).isEqualTo("Rua Nova");
        assertThat(atualizado.getEndereco().getNumero()).isEqualTo("200");
        assertThat(atualizado.getEndereco().getCidade()).isEqualTo("Campinas");
        assertThat(atualizado.getDono()).isEqualTo(dono);

        verify(restauranteGateway).atualizar(restaurante);
    }

    @Test
    @DisplayName("execute - Deve manter o mesmo dono quando idDono for igual ao dono atual")
    void deveManterDonoAtual_quandoIdDonoIgualAoAtual() {
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                ID_RESTAURANTE, "Novo Nome", null, null, null, null, ID_DONO, ID_DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
        when(restauranteGateway.atualizar(any(Restaurante.class))).thenReturn(restaurante);

        useCase.execute(dto);

        verify(usuarioGateway, never()).buscaUsuarioPorId(ID_DONO);
        verify(restauranteGateway).atualizar(restaurante);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando novo proprietário não for encontrado")
    void deveLancarExcecao_quandoNovoDonoNaoEncontrado() {
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                ID_RESTAURANTE, "Novo Nome", null, null, null, null, ID_NOVO_DONO, ID_DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
        when(usuarioGateway.buscaUsuarioPorId(ID_NOVO_DONO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Novo proprietário não encontrado.");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("execute - Deve processar troca de dono desvinculando dono antigo e vinculando novo dono")
    void deveProcessarTrocaDeDonoComSucesso() {
        Usuario novoDonoSemTipo = Usuario.restaurar(ID_NOVO_DONO, "Novo Dono", null);
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                ID_RESTAURANTE, "Restaurante Novo Dono", null, null, null, null, ID_NOVO_DONO, ID_DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
        when(usuarioGateway.buscaUsuarioPorId(ID_NOVO_DONO)).thenReturn(Optional.of(novoDonoSemTipo));
        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(usuarioGateway.buscaUsuarioPorId(ID_NOVO_DONO, ID_RESTAURANTE)).thenReturn(Optional.empty());
        when(restauranteGateway.atualizar(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = useCase.execute(dto);

        assertThat(resultado.getDono()).isEqualTo(novoDonoSemTipo);
        verify(usuarioGateway).desvincularUsuarioAoTipo(ID_DONO, tipoDono.getId());
        verify(usuarioGateway).vincularUsuarioAoTipo(ID_NOVO_DONO, tipoDono.getId());
        verify(restauranteGateway).atualizar(restaurante);
    }

    @Test
    @DisplayName("execute - Deve desvincular tipo prévio do novo dono antes de vincular ao tipo de proprietário")
    void deveDesvincularTipoPrevioDoNovoDono_antesDeVincularNovoTipo() {
        TipoUsuario tipoAntigoDoNovoDono = TipoUsuario.restaurar(600L, restaurante, "Caixa", CategoriaEnun.DONO);
        Usuario novoDonoComTipoAntigo = Usuario.restaurar(ID_NOVO_DONO, "Novo Dono", tipoAntigoDoNovoDono);

        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(
                ID_RESTAURANTE, "Restaurante Troca", null, null, null, null, ID_NOVO_DONO, ID_DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(dono));
        when(restauranteGateway.buscaRestaurantePorId(ID_RESTAURANTE)).thenReturn(Optional.of(restaurante));
        when(usuarioGateway.buscaUsuarioPorId(ID_NOVO_DONO)).thenReturn(Optional.of(novoDonoComTipoAntigo));
        when(usuarioGateway.buscaUsuarioPorId(ID_NOVO_DONO, ID_RESTAURANTE)).thenReturn(Optional.of(novoDonoComTipoAntigo));
        when(restauranteGateway.atualizar(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        useCase.execute(dto);

        verify(usuarioGateway).desvincularUsuarioAoTipo(ID_NOVO_DONO, 600L);
        verify(usuarioGateway).desvincularUsuarioAoTipo(ID_DONO, tipoDono.getId());
        verify(usuarioGateway).vincularUsuarioAoTipo(ID_NOVO_DONO, tipoDono.getId());
        verify(restauranteGateway).atualizar(restaurante);
    }
}
