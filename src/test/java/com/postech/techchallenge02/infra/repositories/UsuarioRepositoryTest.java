package com.postech.techchallenge02.infra.repositories;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.infra.entities.Restaurante;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
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
        Restaurante restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuario tipoUsuario = criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);
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
        Restaurante restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuario tipoUsuario = criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);

        // Act
        boolean existe = usuarioRepository.existeUsuarioComEsteTipoUsuario(tipoUsuario.getId());

        // Assert
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false quando usuários existentes pertencerem a outro tipo de usuário")
    void deveRetornarFalseQuandoUsuariosExistentesPossuemOutroTipoUsuario() {
        // Arrange
        Restaurante restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuario tipoUsuario = criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);
        TipoUsuario outroTipoUsuario = criarTipoUsuario("Cliente", TipoUsuarioEnun.CLIENTE, restaurante);
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
        Restaurante restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuario tipoUsuario = criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);
        criarUsuario("João Silva", "joao@email.com", tipoUsuario);

        // Act
        boolean existe = usuarioRepository.existeUsuarioComEsteTipoUsuario(9999L);

        // Assert
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve salvar e buscar Usuario por id")
    void deveSalvarEBuscarUsuarioPorId() {
        // Arrange
        Restaurante restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuario tipoUsuario = criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos Oliveira " + uniqueId);
        usuario.setEmail("carlos_" + uniqueId + "@email.com");
        usuario.setLogin("carlos.oliveira." + uniqueId);
        usuario.setSenha("senha123");
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        usuario.setTipoUsuario(tipoUsuario);

        // Act
        Usuario salvo = usuarioRepository.save(usuario);
        entityManager.flush();
        entityManager.clear();

        Optional<Usuario> encontrado = usuarioRepository.findById(salvo.getId());

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Carlos Oliveira " + uniqueId);
        assertThat(encontrado.get().getEmail()).isEqualTo("carlos_" + uniqueId + "@email.com");
        assertThat(encontrado.get().getLogin()).isEqualTo("carlos.oliveira." + uniqueId);
        assertThat(encontrado.get().getTipoUsuario()).isNotNull();
        assertThat(encontrado.get().getTipoUsuario().getId()).isEqualTo(tipoUsuario.getId());
    }

    @Test
    @DisplayName("Deve salvar usuário sem vínculo com TipoUsuario")
    void deveSalvarUsuarioSemTipoUsuario() {
        // Arrange
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Sem Cargo " + uniqueId);
        usuario.setEmail("semcargo_" + uniqueId + "@email.com");
        usuario.setLogin("semcargo." + uniqueId);
        usuario.setSenha("senha123");
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        usuario.setTipoUsuario(null);

        // Act
        Usuario salvo = usuarioRepository.save(usuario);
        entityManager.flush();
        entityManager.clear();

        Optional<Usuario> encontrado = usuarioRepository.findById(salvo.getId());

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getTipoUsuario()).isNull();
    }

    @Test
    @DisplayName("Deve excluir Usuario por id com sucesso")
    void deveExcluirUsuarioComSucesso() {
        // Arrange
        Restaurante restaurante = criarRestaurante("Restaurante Padrão");
        TipoUsuario tipoUsuario = criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);
        Usuario usuario = criarUsuario("Lucas Lima", "lucas@email.com", tipoUsuario);
        Long id = usuario.getId();

        // Act
        usuarioRepository.deleteById(id);
        entityManager.flush();
        entityManager.clear();

        Optional<Usuario> aposExclusao = usuarioRepository.findById(id);

        // Assert
        assertThat(aposExclusao).isEmpty();
    }

    // Helper methods para criação isolada de dados de teste
    private Restaurante criarRestaurante(String nome) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Restaurante novoRestaurante = new Restaurante();
        novoRestaurante.setNome(nome + " " + uniqueId);
        novoRestaurante.setTipoCozinha(TipoCozinhaEnun.ITALIANA);
        novoRestaurante.setHorarioFuncionamentoInicio(LocalTime.of(10, 0));
        novoRestaurante.setHorarioFuncionamentoFim(LocalTime.of(22, 0));
        return entityManager.persistAndFlush(novoRestaurante);
    }

    private TipoUsuario criarTipoUsuario(String nome, TipoUsuarioEnun tipo, Restaurante rest) {
        TipoUsuario novoTipo = new TipoUsuario();
        novoTipo.setNome(nome);
        novoTipo.setTipo(tipo);
        novoTipo.setRestaurante(rest);
        return entityManager.persistAndFlush(novoTipo);
    }

    private Usuario criarUsuario(String nome, String email, TipoUsuario tpUsuario) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Usuario usuario = new Usuario();
        usuario.setNome(nome + " " + uniqueId);
        usuario.setEmail(uniqueId + "_" + email);
        usuario.setLogin(nome.toLowerCase().replace(" ", ".") + "." + uniqueId);
        usuario.setSenha("123456");
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        usuario.setTipoUsuario(tpUsuario);
        return entityManager.persistAndFlush(usuario);
    }
}