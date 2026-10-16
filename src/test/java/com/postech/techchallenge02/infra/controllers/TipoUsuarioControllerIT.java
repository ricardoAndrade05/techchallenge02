package com.postech.techchallenge02.infra.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.infra.dtos.AtualizarTipoDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiTipoDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/massa-testes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Testes de Integração - TipoUsuarioController")
public class TipoUsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID com sucesso (200)")
    void deveBuscarPorIdComSucesso() throws Exception {
        Long idExistente = 1L;

        mockMvc.perform(get("/tipos-usuario/{id}", idExistente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idExistente))
                .andExpect(jsonPath("$.nome").exists());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao buscar tipo de usuário inexistente (404)")
    void deveRetornarNotFoundQuandoTipoUsuarioNaoExistir() throws Exception {
        Long idInexistente = 999L;

        mockMvc.perform(get("/tipos-usuario/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar página de tipos de usuário do restaurante com sucesso (200)")
    void deveBuscarTiposUsuarioRestauranteComSucesso() throws Exception {
        Long idRestauranteExistente = 1L;

        mockMvc.perform(get("/tipos-usuario/restaurante/{idRestaurante}", idRestauranteExistente)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(org.hamcrest.Matchers.greaterThan(0)))
                .andExpect(jsonPath("$.totalElements").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    @DisplayName("Deve retornar Not Found ao buscar tipos de usuário de restaurante inexistente (404)")
    void deveRetornarNotFoundQuandoRestauranteNaoExistir() throws Exception {
        Long idRestauranteInexistente = 999L;

        mockMvc.perform(get("/tipos-usuario/restaurante/{idRestaurante}", idRestauranteInexistente)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve inserir novo tipo de usuário com sucesso (201)")
    void deveInserirTipoUsuarioComSucesso() throws Exception {
        NovoTipoUsuarioDTO novoDTO = new NovoTipoUsuarioDTO(1L,"Cozinheiro",CategoriaEnun.DONO,1L);

        mockMvc.perform(post("/tipos-usuario")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Cozinheiro"));
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar inserir tipo de usuário para restaurante inexistente (404)")
    void deveRetornarNotFoundAoInserirParaRestauranteInexistente() throws Exception {
        NovoTipoUsuarioDTO novoDTO = new NovoTipoUsuarioDTO(999L,"Cozinheiro",CategoriaEnun.DONO,1L);

        mockMvc.perform(post("/tipos-usuario")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoDTO)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Deve retornar Conflict ao tentar inserir tipo de usuário já existente (409)")
    void deveRetornarConflictAoInserirTipoUsuarioDuplicado() throws Exception {
        NovoTipoUsuarioDTO dtoDuplicado = new NovoTipoUsuarioDTO(1L,"Gerente do Estabelecimento",CategoriaEnun.DONO,1L);

        mockMvc.perform(post("/tipos-usuario")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoDuplicado)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar Forbidden quando o usuário não tiver permissão para inserir (403)")
    void deveRetornarForbiddenSemPermissao() throws Exception {
        NovoTipoUsuarioDTO dtoSemPermissao = new NovoTipoUsuarioDTO(1L,"Novo Cargo",CategoriaEnun.DONO,2L);

        mockMvc.perform(post("/tipos-usuario")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoSemPermissao)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve excluir tipo de usuário com sucesso (204)")
    void deveExcluirTipoUsuarioComSucesso() throws Exception {
        Long idParaExcluir = 3L; 
        var excluiDTO = new ExcluiTipoDTO(1L);

        mockMvc.perform(delete("/tipos-usuario/{id}", idParaExcluir)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(excluiDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar excluir tipo de usuário inexistente (404)")
    void deveRetornarNotFoundAoExcluirTipoInexistente() throws Exception {
        Long idInexistente = 999L;
        var excluiDTO = new ExcluiTipoDTO(1L);

        mockMvc.perform(delete("/tipos-usuario/{id}", idInexistente)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(excluiDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Conflict ao tentar excluir tipo de usuário vinculado a um usuário (409)")
    void deveRetornarConflictAoExcluirTipoVinculado() throws Exception {
        Long idVinculado = 1L;
        var excluiDTO = new ExcluiTipoDTO(1L);

        mockMvc.perform(delete("/tipos-usuario/{id}", idVinculado)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(excluiDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar Forbidden quando o usuário solicitante não tiver permissão para excluir (403)")
    void deveRetornarForbiddenAoExcluirSemPermissao() throws Exception {
        Long idParaExcluir = 2L;
        var excluiDTO = new ExcluiTipoDTO(2L);

        mockMvc.perform(delete("/tipos-usuario/{id}", idParaExcluir)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(excluiDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário com sucesso (200)")
    void deveAtualizarTipoUsuarioComSucesso() throws Exception {
        Long idParaAtualizar = 2L; 
        var atualizarDTO = new AtualizarTipoDTO("Cliente VIP",1L,1L );

        mockMvc.perform(put("/tipos-usuario/{id}", idParaAtualizar)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idParaAtualizar))
                .andExpect(jsonPath("$.nome").value("Cliente VIP"));
    }

    @Test
    @DisplayName("Deve retornar Forbidden ao tentar atualizar sem permissão (403)")
    void deveRetornarForbiddenAoAtualizarSemPermissao() throws Exception {
        Long idParaAtualizar = 2L;
        var atualizarDTO = new AtualizarTipoDTO("Cliente VIP",2L,1L);

        mockMvc.perform(put("/tipos-usuario/{id}", idParaAtualizar)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizarDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar atualizar tipo de usuário inexistente (404)")
    void deveRetornarNotFoundAoAtualizarTipoInexistente() throws Exception {
        Long idInexistente = 999L;
        var atualizarDTO = new AtualizarTipoDTO("Nome Novo",1L,1L);

        mockMvc.perform(put("/tipos-usuario/{id}", idInexistente)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizarDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Conflict ao atualizar para um tipo de usuário já existente (409)")
    void deveRetornarConflictAoAtualizarParaNomeExistente() throws Exception {
        Long idParaAtualizar = 2L;
        var atualizarDTO = new AtualizarTipoDTO("Cliente Usual",1L,1L);

        mockMvc.perform(put("/tipos-usuario/{id}", idParaAtualizar)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizarDTO)))
                .andExpect(status().isConflict());
    }
    
}
