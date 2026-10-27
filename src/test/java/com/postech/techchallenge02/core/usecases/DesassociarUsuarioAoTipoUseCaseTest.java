package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("DesassociarUsuarioAoTipoUseCase")
class DesassociarUsuarioAoTipoUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private DesassociarUsuarioAoTipoUseCase useCase;

    private static final Long ID_USUARIO_LOGADO = 1L;
    private static final Long ID_RESTAURANTE = 10L;
    private static final Long ID_USUARIO_A_DESASSOCIAR = 2L;
    private static final Long ID_TIPO_USUARIO = 100L;

    private Restaurante restaurante;
    private TipoUsuario tipoDono;
    private TipoUsuario tipoFuncionario;
    private Usuario usuarioLogado;
    private Usuario usuarioADesassociar;
    private DesassociarUsuarioTipoDTO dto;

    @BeforeEach
    void setUp() {
        useCase = DesassociarUsuarioAoTipoUseCase.getInstance(usuarioGateway);

        restaurante = Restaurante.restaurar(ID_RESTAURANTE, "Restaurante Teste", null, null, null, null, null);
        tipoDono = TipoUsuario.restaurar(50L, restaurante, "Proprietário", CategoriaEnun.DONO);
        tipoFuncionario = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, "Garçom", CategoriaEnun.DONO);

        usuarioLogado = Usuario.restaurar(ID_USUARIO_LOGADO, "Gerente", tipoDono);
        usuarioADesassociar = Usuario.restaurar(ID_USUARIO_A_DESASSOCIAR, "Garçom 1", tipoFuncionario);

        dto = new DesassociarUsuarioTipoDTO(ID_USUARIO_LOGADO, ID_RESTAURANTE, ID_USUARIO_A_DESASSOCIAR);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o usuário logado não for encontrado")
    void deveLancarExcecao_quandoUsuarioLogadoNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuario não encontrado.");

        verify(usuarioGateway, never()).desvincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando o usuário logado não tiver permissão para atualizar")
    void deveLancarExcecao_quandoUsuarioLogadoSemPermissao() {
        TipoUsuario tipoCliente = TipoUsuario.restaurar(60L, restaurante, "Cliente", CategoriaEnun.CLIENTE);
        Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_LOGADO, "Cliente", tipoCliente);
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioCliente));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("Usuário não tem permissão para realizar esta ação.");

        verify(usuarioGateway, never()).desvincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o usuário a ser desassociado não for encontrado")
    void deveLancarExcecao_quandoUsuarioADesassociarNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_A_DESASSOCIAR, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuario a ser desassociado não encontrado.");

        verify(usuarioGateway, never()).desvincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando usuário não possuir vínculo com o restaurante")
    void deveLancarExcecao_quandoUsuarioNaoPossuiVinculoComRestaurante() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_A_DESASSOCIAR, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioADesassociar));
        when(usuarioGateway.usuarioVinculadoAoRestaurante(ID_USUARIO_A_DESASSOCIAR, ID_RESTAURANTE)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuario não possui tipo com o restaurante para ser desassociado.");

        verify(usuarioGateway, never()).desvincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve desvincular o usuário com sucesso")
    void deveDesvincularUsuarioComSucesso() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_A_DESASSOCIAR, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioADesassociar));
        when(usuarioGateway.usuarioVinculadoAoRestaurante(ID_USUARIO_A_DESASSOCIAR, ID_RESTAURANTE)).thenReturn(true);

        assertThatCode(() -> useCase.execute(dto)).doesNotThrowAnyException();

        verify(usuarioGateway).desvincularUsuarioAoTipo(ID_USUARIO_A_DESASSOCIAR, ID_TIPO_USUARIO);
    }
}
