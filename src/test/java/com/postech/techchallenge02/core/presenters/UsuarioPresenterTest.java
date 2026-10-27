package com.postech.techchallenge02.core.presenters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;

@DisplayName("UsuarioPresenter")
class UsuarioPresenterTest {

    private static final Long ID_USUARIO = 1L;
    private static final String NOME_USUARIO = "Carlos Silva";
    private static final Long ID_TIPO = 10L;
    private static final String NOME_TIPO = "Gerente";
    private static final CategoriaEnun TIPO_CATEGORIA = CategoriaEnun.DONO;

    @Test
    @DisplayName("toDTO - Deve converter Usuario para UsuarioDTO corretamente com todos os campos")
    void deveConverterParaDTO_comDadosCompletos() {
        Restaurante restaurante = Restaurante.restaurar(100L, "Restaurante Teste", null, null, null, null, null);
        TipoUsuario tipoUsuario = TipoUsuario.restaurar(ID_TIPO, restaurante, NOME_TIPO, TIPO_CATEGORIA);
        Usuario usuario = Usuario.restaurar(ID_USUARIO, NOME_USUARIO, tipoUsuario);

        UsuarioDTO dto = UsuarioPresenter.toDTO(usuario);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(ID_USUARIO);
        assertThat(dto.nome()).isEqualTo(NOME_USUARIO);

        assertThat(dto.tipoUsuario()).isNotNull();
        assertThat(dto.tipoUsuario().id()).isEqualTo(ID_TIPO);
        assertThat(dto.tipoUsuario().nome()).isEqualTo(NOME_TIPO);
        assertThat(dto.tipoUsuario().tipo()).isEqualTo(TIPO_CATEGORIA);
        assertThat(dto.tipoUsuario().restaurante()).isNull();
    }

    @Test
    @DisplayName("toDTO - Deve lançar NullPointerException quando o usuário for nulo")
    void deveLancarExcecao_quandoUsuarioNulo() {
        assertThatThrownBy(() -> UsuarioPresenter.toDTO(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("toDTO - Deve lançar NullPointerException quando o tipo do usuário for nulo")
    void deveLancarExcecao_quandoTipoUsuarioNulo() {
        Usuario usuarioSemTipo = Usuario.restaurar(ID_USUARIO, NOME_USUARIO, null);

        assertThatThrownBy(() -> UsuarioPresenter.toDTO(usuarioSemTipo))
                .isInstanceOf(NullPointerException.class);
    }
}
