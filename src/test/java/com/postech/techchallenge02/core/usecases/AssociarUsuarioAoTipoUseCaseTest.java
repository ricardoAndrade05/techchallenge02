package com.postech.techchallenge02.core.usecases;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioJaVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssociarUsuarioAoTipoUseCase")
class AssociarUsuarioAoTipoUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    private AssociarUsuarioAoTipoUseCase useCase;

    private static final Long ID_USUARIO_LOGADO = 1L;
    private static final Long ID_USUARIO_A_ASSOCIAR = 2L;
    private static final Long ID_TIPO_USUARIO = 10L;
    private static final Long ID_RESTAURANTE = 100L;
    private static final Long ID_OUTRO_RESTAURANTE = 200L;

    private Restaurante restaurante;
    private TipoUsuario tipoUsuario;
    private Usuario usuarioLogado;
    private Usuario usuarioAAssociar;
    private AssociarUsuarioAoTipoDTO dto;

    @BeforeEach
    void setUp() {
        useCase = AssociarUsuarioAoTipoUseCase.getInstance(usuarioGateway, tipoUsuarioGateway);

        restaurante = Restaurante.restaurar(ID_RESTAURANTE, "Restaurante Teste", null, null, null, null, null);
        tipoUsuario = TipoUsuario.restaurar(ID_TIPO_USUARIO, restaurante, "Gerente", CategoriaEnun.DONO);
        usuarioLogado = Usuario.restaurar(ID_USUARIO_LOGADO, "Gerente Dono", tipoUsuario);
        usuarioAAssociar = Usuario.restaurar(ID_USUARIO_A_ASSOCIAR, "Novo Funcionario", null);

        dto = new AssociarUsuarioAoTipoDTO(ID_USUARIO_LOGADO, ID_USUARIO_A_ASSOCIAR, ID_TIPO_USUARIO, ID_RESTAURANTE);
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o usuário logado não for encontrado")
    void deveLancarExcecao_quandoUsuarioLogadoNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuario não encontrado.");

        verify(usuarioGateway, never()).vincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando usuário logado não tiver permissão para cadastrar")
    void deveLancarExcecao_quandoUsuarioLogadoSemPermissao() {
        TipoUsuario tipoCliente = TipoUsuario.restaurar(20L, restaurante, "Cliente", CategoriaEnun.CLIENTE);
        Usuario usuarioCliente = Usuario.restaurar(ID_USUARIO_LOGADO, "Cliente", tipoCliente);
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioCliente));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("Usuário não tem permissão para realizar esta ação.");

        verify(usuarioGateway, never()).vincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando tipo de usuário não for encontrado")
    void deveLancarExcecao_quandoTipoUsuarioNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Tipo de usuario não encontrado.");

        verify(usuarioGateway, never()).vincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioSemPermissaoException quando o tipo pertence a outro restaurante")
    void deveLancarExcecao_quandoTipoUsuarioDeOutroRestaurante() {
        Restaurante outroRestaurante = Restaurante.restaurar(ID_OUTRO_RESTAURANTE, "Outro Restaurante", null, null, null, null, null);
        TipoUsuario tipoOutroRestaurante = TipoUsuario.restaurar(ID_TIPO_USUARIO, outroRestaurante, "Gerente", CategoriaEnun.DONO);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoOutroRestaurante));

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioSemPermissaoException.class)
                .hasMessageContaining("O tipo usuario não pertence ao restaurante do usuario logado.");

        verify(usuarioGateway, never()).vincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar RecursoNaoEncontradoException quando o usuário a ser vinculado não for encontrado")
    void deveLancarExcecao_quandoUsuarioAAssociarNaoEncontrado() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_A_ASSOCIAR)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuario a ser vinculado não encontrado.");

        verify(usuarioGateway, never()).vincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve lançar UsuarioJaVinculadoException quando usuário já tiver tipo vinculado no restaurante")
    void deveLancarExcecao_quandoUsuarioJaVinculadoAoRestaurante() {
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_A_ASSOCIAR)).thenReturn(Optional.of(usuarioAAssociar));
        when(usuarioGateway.usuarioVinculadoAoRestaurante(ID_USUARIO_A_ASSOCIAR, ID_RESTAURANTE)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(UsuarioJaVinculadoException.class)
                .hasMessageContaining("Usuario já possui um tipo de usuario cadastrado para este restaurante.");

        verify(usuarioGateway, never()).vincularUsuarioAoTipo(any(), any());
    }

    @Test
    @DisplayName("execute - Deve vincular o usuário ao tipo com sucesso e retornar o usuário atualizado")
    void deveVincularUsuarioAoTipoComSucesso() {
        Usuario usuarioVinculado = Usuario.restaurar(ID_USUARIO_A_ASSOCIAR, "Novo Funcionario", tipoUsuario);

        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_LOGADO, ID_RESTAURANTE)).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioGateway.buscaTipoUsuarioPorId(ID_TIPO_USUARIO)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioGateway.buscaUsuarioPorId(ID_USUARIO_A_ASSOCIAR)).thenReturn(Optional.of(usuarioAAssociar));
        when(usuarioGateway.usuarioVinculadoAoRestaurante(ID_USUARIO_A_ASSOCIAR, ID_RESTAURANTE)).thenReturn(false);
        when(usuarioGateway.vincularUsuarioAoTipo(ID_USUARIO_A_ASSOCIAR, ID_TIPO_USUARIO)).thenReturn(usuarioVinculado);

        Usuario resultado = useCase.execute(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(ID_USUARIO_A_ASSOCIAR);
        assertThat(resultado.getTipoUsuario()).isEqualTo(tipoUsuario);

        verify(usuarioGateway).vincularUsuarioAoTipo(ID_USUARIO_A_ASSOCIAR, ID_TIPO_USUARIO);
    }
}
