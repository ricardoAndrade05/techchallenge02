package com.postech.techchallenge02.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.techchallenge02.core.enums.CategoriaEnun;

@DisplayName("Usuario")
class UsuarioTest {

    private static final Long ID = 1L;
    private static final String NOME = "Carlos Silva";

    private TipoUsuario tipoDono() {
        return TipoUsuario.restaurar(10L, null, "Proprietário", CategoriaEnun.DONO);
    }

    private TipoUsuario tipoCliente() {
        return TipoUsuario.restaurar(20L, null, "Cliente", CategoriaEnun.CLIENTE);
    }

    @Test
    @DisplayName("restaurar - Deve restaurar instância de usuário com id, nome e tipo de usuário")
    void deveRestaurarUsuario_comDadosCompletos() {
        TipoUsuario tipo = tipoDono();

        Usuario usuario = Usuario.restaurar(ID, NOME, tipo);

        assertThat(usuario.getId()).isEqualTo(ID);
        assertThat(usuario.getNome()).isEqualTo(NOME);
        assertThat(usuario.getTipoUsuario()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("podeCadastrar - Deve retornar true quando tipo de usuário for DONO")
    void deveRetornarTrueEmPodeCadastrar_quandoTipoDono() {
        Usuario usuario = Usuario.restaurar(ID, NOME, tipoDono());

        assertThat(usuario.podeCadastrar()).isTrue();
    }

    @Test
    @DisplayName("podeCadastrar - Deve retornar false quando tipo de usuário for CLIENTE")
    void deveRetornarFalseEmPodeCadastrar_quandoTipoCliente() {
        Usuario usuario = Usuario.restaurar(ID, NOME, tipoCliente());

        assertThat(usuario.podeCadastrar()).isFalse();
    }

    @Test
    @DisplayName("podeCadastrar - Deve retornar false quando tipo de usuário for nulo")
    void deveRetornarFalseEmPodeCadastrar_quandoTipoNulo() {
        Usuario usuario = Usuario.restaurar(ID, NOME, null);

        assertThat(usuario.podeCadastrar()).isFalse();
    }

    @Test
    @DisplayName("podeExcluir - Deve retornar true quando tipo de usuário for DONO")
    void deveRetornarTrueEmPodeExcluir_quandoTipoDono() {
        Usuario usuario = Usuario.restaurar(ID, NOME, tipoDono());

        assertThat(usuario.podeExcluir()).isTrue();
    }

    @Test
    @DisplayName("podeExcluir - Deve retornar false quando tipo de usuário for CLIENTE")
    void deveRetornarFalseEmPodeExcluir_quandoTipoCliente() {
        Usuario usuario = Usuario.restaurar(ID, NOME, tipoCliente());

        assertThat(usuario.podeExcluir()).isFalse();
    }

    @Test
    @DisplayName("podeExcluir - Deve retornar false quando tipo de usuário for nulo")
    void deveRetornarFalseEmPodeExcluir_quandoTipoNulo() {
        Usuario usuario = Usuario.restaurar(ID, NOME, null);

        assertThat(usuario.podeExcluir()).isFalse();
    }

    @Test
    @DisplayName("podeAtualizar - Deve retornar true quando tipo de usuário for DONO")
    void deveRetornarTrueEmPodeAtualizar_quandoTipoDono() {
        Usuario usuario = Usuario.restaurar(ID, NOME, tipoDono());

        assertThat(usuario.podeAtualizar()).isTrue();
    }

    @Test
    @DisplayName("podeAtualizar - Deve retornar false quando tipo de usuário for CLIENTE")
    void deveRetornarFalseEmPodeAtualizar_quandoTipoCliente() {
        Usuario usuario = Usuario.restaurar(ID, NOME, tipoCliente());

        assertThat(usuario.podeAtualizar()).isFalse();
    }

    @Test
    @DisplayName("podeAtualizar - Deve retornar false quando tipo de usuário for nulo")
    void deveRetornarFalseEmPodeAtualizar_quandoTipoNulo() {
        Usuario usuario = Usuario.restaurar(ID, NOME, null);

        assertThat(usuario.podeAtualizar()).isFalse();
    }

    @Test
    @DisplayName("equals e hashCode - Deve considerar iguais instâncias com o mesmo id")
    void deveConsiderarIguais_quandoMesmoId() {
        Usuario u1 = Usuario.restaurar(1L, "Nome 1", tipoDono());
        Usuario u2 = Usuario.restaurar(1L, "Nome 2", tipoCliente());

        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }

    @Test
    @DisplayName("equals - Deve retornar true para a mesma referência")
    void deveRetornarTrue_quandoMesmaReferencia() {
        Usuario u1 = Usuario.restaurar(1L, NOME, tipoDono());

        assertThat(u1.equals(u1)).isTrue();
    }

    @Test
    @DisplayName("equals - Deve retornar false quando comparado com null ou outra classe")
    void deveRetornarFalse_quandoComparadoComNullOuOutraClasse() {
        Usuario u1 = Usuario.restaurar(1L, NOME, tipoDono());

        assertThat(u1.equals(null)).isFalse();
        assertThat(u1.equals("String")).isFalse();
    }

    @Test
    @DisplayName("equals - Deve retornar false quando ids forem diferentes")
    void deveRetornarFalse_quandoIdsDiferentes() {
        Usuario u1 = Usuario.restaurar(1L, NOME, tipoDono());
        Usuario u2 = Usuario.restaurar(2L, NOME, tipoDono());

        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    @DisplayName("equals - Deve tratar id nulo corretamente")
    void deveTratarIdNuloCorretamenteNoEquals() {
        Usuario uSemId1 = Usuario.restaurar(null, NOME, tipoDono());
        Usuario uSemId2 = Usuario.restaurar(null, NOME, tipoDono());
        Usuario uComId = Usuario.restaurar(1L, NOME, tipoDono());

        assertThat(uSemId1).isEqualTo(uSemId2);
        assertThat(uSemId1.hashCode()).isEqualTo(uSemId2.hashCode());
        assertThat(uSemId1).isNotEqualTo(uComId);
        assertThat(uComId).isNotEqualTo(uSemId1);
    }
}
