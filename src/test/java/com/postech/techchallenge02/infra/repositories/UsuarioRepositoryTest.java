package com.postech.techchallenge02.infra.repositories;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.infra.jpa.entities.RestauranteEntity;
import com.postech.techchallenge02.infra.jpa.entities.TipoUsuarioEntity;
import com.postech.techchallenge02.infra.jpa.entities.Usuario;
import com.postech.techchallenge02.infra.jpa.repositories.UsuarioRepository;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@ActiveProfiles("test")
@DisplayName("Testes de Integração - UsuarioRepository")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve retornar true quando existir usuário associado ao idTipoUsuario informado")
    void deveRetornarTrueQuandoExistirUsuarioComOTipoUsuarioInformado() {
        // Arrange
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoUsuario = criarTipoUsuario("Gerente", CategoriaEnun.DONO, restaurante);
        criarUsuario("João Silva", "joao@email.com", tipoUsuario);

        // Act
        boolean existe = usuarioRepository.existeUsuarioComEsteTipoUsuario(tipoUsuario.getId());

        // Assert
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não houver nenhum usuário associado ao idTipoUsuario")
    void deveRetornarFalseQuandoNaoExistirUsuarioComOTipoUsuarioInformado() {
        // Arrange
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoUsuario = criarTipoUsuario("Gerente", CategoriaEnun.DONO, restaurante);

        // Act
        boolean existe = usuarioRepository.existeUsuarioComEsteTipoUsuario(tipoUsuario.getId());

        // Assert
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false quando usuários existentes pertencerem a outro tipo de usuário")
    void deveRetornarFalseQuandoUsuariosExistentesPossuemOutroTipoUsuario() {
        // Arrange
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoUsuario = criarTipoUsuario("Gerente", CategoriaEnun.DONO, restaurante);
        TipoUsuarioEntity outroTipoUsuario = criarTipoUsuario("Cliente", CategoriaEnun.CLIENTE, restaurante);
        criarUsuario("Maria Souza", "maria@email.com", outroTipoUsuario);

        // Act
        boolean existe = usuarioRepository.existeUsuarioComEsteTipoUsuario(tipoUsuario.getId());

        // Assert
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false quando informado um id de tipo de usuário inexistente")
    void deveRetornarFalseQuandoIdTipoUsuarioForInexistente() {
        // Arrange
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoUsuario = criarTipoUsuario("Gerente", CategoriaEnun.DONO, restaurante);
        criarUsuario("João Silva", "joao@email.com", tipoUsuario);

        // Act
        boolean existe = usuarioRepository.existeUsuarioComEsteTipoUsuario(9999L);

        // Assert
        assertThat(existe).isFalse();
    }

    // --- usuarioEstaVinculadoAoRestaurante ---

    @Test
    @DisplayName("usuarioEstaVinculadoAoRestaurante - Deve retornar true quando o usuário tiver tipo vinculado ao restaurante")
    void deveRetornarTrueQuandoUsuarioEstiverVinculadoAoRestaurante() {
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoUsuario = criarTipoUsuario("Garçom", CategoriaEnun.DONO, restaurante);
        Usuario usuario = criarUsuario("Carlos Santos", "carlos@email.com", tipoUsuario);

        boolean vinculado = usuarioRepository.usuarioEstaVinculadoAoRestaurante(usuario.getId(), restaurante.getId());

        assertThat(vinculado).isTrue();
    }

    @Test
    @DisplayName("usuarioEstaVinculadoAoRestaurante - Deve retornar false quando o usuário tiver tipo vinculado a outro restaurante")
    void deveRetornarFalseQuandoUsuarioEstiverVinculadoAOutroRestaurante() {
        RestauranteEntity restaurante1 = criarRestaurante("Restaurante 1");
        RestauranteEntity restaurante2 = criarRestaurante("Restaurante 2");
        TipoUsuarioEntity tipoUsuario1 = criarTipoUsuario("Garçom", CategoriaEnun.DONO, restaurante1);
        Usuario usuario = criarUsuario("Carlos Santos", "carlos@email.com", tipoUsuario1);

        boolean vinculado = usuarioRepository.usuarioEstaVinculadoAoRestaurante(usuario.getId(), restaurante2.getId());

        assertThat(vinculado).isFalse();
    }

    @Test
    @DisplayName("usuarioEstaVinculadoAoRestaurante - Deve retornar false quando o usuário não tiver nenhum tipo de usuário vinculado")
    void deveRetornarFalseQuandoUsuarioNaoPossuirTiposVinculados() {
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        Usuario usuarioSemTipo = criarUsuarioSemTipo("Ana Paula", "ana@email.com");

        boolean vinculado = usuarioRepository.usuarioEstaVinculadoAoRestaurante(usuarioSemTipo.getId(), restaurante.getId());

        assertThat(vinculado).isFalse();
    }

    @Test
    @DisplayName("usuarioEstaVinculadoAoRestaurante - Deve retornar false quando o ID do usuário não existir")
    void deveRetornarFalseQuandoIdUsuarioInexistenteAoVerificarVinculo() {
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");

        boolean vinculado = usuarioRepository.usuarioEstaVinculadoAoRestaurante(9999L, restaurante.getId());

        assertThat(vinculado).isFalse();
    }

    // --- findAllByTiposUsuarioIn ---

    @Test
    @DisplayName("findAllByTiposUsuarioIn - Deve retornar usuários associados aos tipos de usuário informados")
    void deveRetornarUsuariosQuandoPertenceremAosTiposInformados() {
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoGerente = criarTipoUsuario("Gerente", CategoriaEnun.DONO, restaurante);
        TipoUsuarioEntity tipoGarcom = criarTipoUsuario("Garçom", CategoriaEnun.DONO, restaurante);
        TipoUsuarioEntity tipoOutro = criarTipoUsuario("Outro", CategoriaEnun.DONO, restaurante);

        Usuario usuario1 = criarUsuario("User Gerente", "gerente@email.com", tipoGerente);
        Usuario usuario2 = criarUsuario("User Garçom", "garcom@email.com", tipoGarcom);
        Usuario usuario3 = criarUsuario("User Outro", "outro@email.com", tipoOutro);

        List<Usuario> resultado = usuarioRepository.findAllByTiposUsuarioIn(List.of(tipoGerente, tipoGarcom));

        assertThat(resultado).isNotNull().hasSize(2);
        assertThat(resultado).extracting(Usuario::getId).contains(usuario1.getId(), usuario2.getId());
        assertThat(resultado).extracting(Usuario::getId).doesNotContain(usuario3.getId());
    }

    @Test
    @DisplayName("findAllByTiposUsuarioIn - Deve retornar lista vazia quando nenhum usuário pertencer aos tipos informados")
    void deveRetornarListaVaziaQuandoNenhumUsuarioPertencerAosTiposInformados() {
        RestauranteEntity restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuarioEntity tipoSemUsuarios = criarTipoUsuario("Sem Usuários", CategoriaEnun.DONO, restaurante);

        List<Usuario> resultado = usuarioRepository.findAllByTiposUsuarioIn(List.of(tipoSemUsuarios));

        assertThat(resultado).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findAllByTiposUsuarioIn - Deve retornar lista vazia quando a lista de tipos for vazia")
    void deveRetornarListaVaziaQuandoListaDeTiposForVazia() {
        List<Usuario> resultado = usuarioRepository.findAllByTiposUsuarioIn(List.of());

        assertThat(resultado).isNotNull().isEmpty();
    }

    // Helper methods para criação isolada de dados de teste
    private RestauranteEntity criarRestaurante(String nome) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        RestauranteEntity novoRestaurante = new RestauranteEntity();
        novoRestaurante.setNome(nome + " " + uniqueId);
        novoRestaurante.setTipoCozinha(TipoCozinhaEnun.ITALIANA);
        novoRestaurante.setHorarioFuncionamentoInicio(LocalTime.of(10, 0));
        novoRestaurante.setHorarioFuncionamentoFim(LocalTime.of(22, 0));
        return entityManager.persistAndFlush(novoRestaurante);
    }

    private TipoUsuarioEntity criarTipoUsuario(String nome, CategoriaEnun tipo, RestauranteEntity rest) {
        TipoUsuarioEntity novoTipo = new TipoUsuarioEntity();
        novoTipo.setNome(nome);
        novoTipo.setTipo(tipo);
        novoTipo.setRestaurante(rest);
        return entityManager.persistAndFlush(novoTipo);
    }

    private Usuario criarUsuario(String nome, String email, TipoUsuarioEntity tpUsuario) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Usuario usuario = new Usuario();
        usuario.setNome(nome + " " + uniqueId);
        usuario.setEmail(uniqueId + "_" + email);
        usuario.setLogin(nome.toLowerCase().replace(" ", ".") + "." + uniqueId);
        usuario.setSenha("123456");
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        usuario.addTiposUsuario(tpUsuario);
        return entityManager.persistAndFlush(usuario);
    }

    private Usuario criarUsuarioSemTipo(String nome, String email) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Usuario usuario = new Usuario();
        usuario.setNome(nome + " " + uniqueId);
        usuario.setEmail(uniqueId + "_" + email);
        usuario.setLogin(nome.toLowerCase().replace(" ", ".") + "." + uniqueId);
        usuario.setSenha("123456");
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        return entityManager.persistAndFlush(usuario);
    }
}