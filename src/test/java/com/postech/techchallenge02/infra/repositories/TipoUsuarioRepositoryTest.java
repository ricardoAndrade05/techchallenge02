package com.postech.techchallenge02.infra.repositories;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.infra.entities.Endereco;
import com.postech.techchallenge02.infra.entities.Restaurante;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@DisplayName("Testes de Integração - TipoUsuarioRepository")
class TipoUsuarioRepositoryTest {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar true quando existir TipoUsuario com o mesmo nome, restaurante e tipo")
    void deveRetornarTrueQuandoExistirTipoUsuarioComMesmoNomeRestauranteETipo() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");
        criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);

        boolean existe = tipoUsuarioRepository.existeTipoUsuario("Gerente", restaurante.getId(), TipoUsuarioEnun.DONO);

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando o nome for diferente")
    void deveRetornarFalseQuandoNomeForDiferente() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");
        criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);

        boolean existe = tipoUsuarioRepository.existeTipoUsuario("Atendente", restaurante.getId(), TipoUsuarioEnun.DONO);

        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false quando o restaurante for diferente")
    void deveRetornarFalseQuandoRestauranteForDiferente() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");
        Restaurante outroRestaurante = criarRestaurante("Outro Restaurante");
        criarTipoUsuario("Gerente", TipoUsuarioEnun.DONO, restaurante);

        boolean existe = tipoUsuarioRepository.existeTipoUsuario("Gerente", outroRestaurante.getId(), TipoUsuarioEnun.DONO);

        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false quando o tipo enum for diferente")
    void deveRetornarFalseQuandoTipoUsuarioForDiferente() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");
        criarTipoUsuario("Cliente VIP", TipoUsuarioEnun.CLIENTE, restaurante);

        boolean existe = tipoUsuarioRepository.existeTipoUsuario("Cliente VIP", restaurante.getId(), TipoUsuarioEnun.DONO);

        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Deve retornar página com os tipos de usuário vinculados ao restaurante informado")
    void deveRetornarPaginaComTiposDeUsuarioDoRestaurante() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");
        criarTipoUsuario("Administrador", TipoUsuarioEnun.DONO, restaurante);
        criarTipoUsuario("Cliente Comum", TipoUsuarioEnun.CLIENTE, restaurante);

        Pageable pageable = PageRequest.of(0, 10);
        Page<TipoUsuario> resultado = tipoUsuarioRepository.buscaTiposUsuarioPorRestaurante(restaurante.getId(), pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando o restaurante não possui tipos de usuário")
    void deveRetornarPaginaVaziaQuandoRestauranteNaoPossuiTiposDeUsuario() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");

        Pageable pageable = PageRequest.of(0, 10);
        Page<TipoUsuario> resultado = tipoUsuarioRepository.buscaTiposUsuarioPorRestaurante(restaurante.getId(), pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar e buscar TipoUsuario por id")
    void deveSalvarEBuscarTipoUsuarioPorId() {
        Restaurante restaurante = criarRestaurante("Restaurante Teste");

        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setNome("Supervisor");
        tipoUsuario.setTipo(TipoUsuarioEnun.DONO);
        tipoUsuario.setRestaurante(restaurante);

        TipoUsuario salvo = tipoUsuarioRepository.save(tipoUsuario);
        entityManager.flush();
        entityManager.clear();

        Optional<TipoUsuario> encontrado = tipoUsuarioRepository.findById(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Supervisor");
    }

    private Restaurante criarRestaurante(String nome) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setCidade("Maringá");
        endereco.setEstado("PR");
        endereco.setCep("87020-010");
        entityManager.persist(endereco);

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        Usuario dono = new Usuario();
        dono.setNome("Dono Teste " + uniqueId);
        dono.setEmail("dono_" + uniqueId + "@teste.com");
        dono.setLogin("dono.teste." + uniqueId);
        dono.setSenha("123456");
        entityManager.persist(dono);

        Restaurante novoRestaurante = new Restaurante();
        novoRestaurante.setNome(nome + " " + uniqueId);
        novoRestaurante.setTipoCozinha(TipoCozinhaEnun.ITALIANA);
        novoRestaurante.setHorarioFuncionamentoInicio(LocalTime.of(10, 0));
        novoRestaurante.setHorarioFuncionamentoFim(LocalTime.of(22, 0));
        novoRestaurante.setEndereco(endereco);
        novoRestaurante.setDono(dono);
        
        entityManager.persist(novoRestaurante);
        entityManager.flush();
        return novoRestaurante;
    }

    private TipoUsuario criarTipoUsuario(String nome, TipoUsuarioEnun tipo, Restaurante rest) {
        TipoUsuario novoTipo = new TipoUsuario();
        novoTipo.setNome(nome);
        novoTipo.setTipo(tipo);
        novoTipo.setRestaurante(rest);
        entityManager.persist(novoTipo);
        entityManager.flush();
        return novoTipo;
    }
}