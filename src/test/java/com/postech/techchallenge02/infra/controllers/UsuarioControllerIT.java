package com.postech.techchallenge02.infra.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/massa-testes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Testes de Integração - UsuarioController")
public class UsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- PUT /usuarios/associar-tipo ---

    @Test
    @DisplayName("Deve associar usuário ao tipo com sucesso (200)")
    void deveAssociarUsuarioAoTipoComSucesso() throws Exception {
        // Usuário 1 (Carlos - Dono do Restaurante 1) associa Usuário 6 (Fernanda) ao Tipo 2 (Gerente de Atendimento - Restaurante 1)
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(1L, 6L, 2L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6L))
                .andExpect(jsonPath("$.tipoUsuario.id").value(2L))
                .andExpect(jsonPath("$.tipoUsuario.nome").value("Gerente de Atendimento"));
    }

    @Test
    @DisplayName("Deve retornar Not Found quando usuário logado não for encontrado no restaurante (404)")
    void deveRetornarNotFoundQuandoUsuarioLogadoNaoEncontrado() throws Exception {
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(999L, 6L, 2L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found quando usuário a ser associado não for encontrado (404)")
    void deveRetornarNotFoundQuandoUsuarioAAssociarNaoEncontrado() throws Exception {
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(1L, 999L, 2L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found quando tipo de usuário não for encontrado (404)")
    void deveRetornarNotFoundQuandoTipoUsuarioNaoEncontrado() throws Exception {
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(1L, 6L, 999L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Forbidden quando usuário logado for do tipo CLIENTE (403)")
    void deveRetornarForbiddenQuandoUsuarioLogadoSemPermissao() throws Exception {
        // Usuário 5 (Lucas - Cliente no Restaurante 1)
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(5L, 6L, 2L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar Forbidden quando o tipo pertencer a outro restaurante (403)")
    void deveRetornarForbiddenQuandoTipoPertenceAOutroRestaurante() throws Exception {
        // Tipo 4 pertence ao Restaurante 2 (Sushiman), mas idRestaurante informado é 1
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(1L, 6L, 4L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar Conflict quando usuário já possuir tipo vinculado ao restaurante (409)")
    void deveRetornarConflictQuandoUsuarioJaVinculadoAoRestaurante() throws Exception {
        // Usuário 5 (Lucas) já está vinculado ao Restaurante 1 (Tipo 3 - Cliente VIP)
        AssociarUsuarioAoTipoDTO dto = new AssociarUsuarioAoTipoDTO(1L, 5L, 2L, 1L);

        mockMvc.perform(put("/usuarios/associar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    // --- PUT /usuarios/desassociar-tipo ---

    @Test
    @DisplayName("Deve desassociar usuário do tipo com sucesso (204)")
    void deveDesassociarUsuarioDoTipoComSucesso() throws Exception {
        // Usuário 1 (Dono) desassocia Usuário 5 (vinculado ao Tipo 3 no Restaurante 1)
        DesassociarUsuarioTipoDTO dto = new DesassociarUsuarioTipoDTO(1L, 1L, 5L);

        mockMvc.perform(put("/usuarios/desassociar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar desassociar com usuário logado inexistente (404)")
    void deveRetornarNotFoundAoDesassociarComUsuarioLogadoInexistente() throws Exception {
        DesassociarUsuarioTipoDTO dto = new DesassociarUsuarioTipoDTO(999L, 1L, 5L);

        mockMvc.perform(put("/usuarios/desassociar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar desassociar usuário inexistente (404)")
    void deveRetornarNotFoundAoDesassociarUsuarioInexistente() throws Exception {
        DesassociarUsuarioTipoDTO dto = new DesassociarUsuarioTipoDTO(1L, 1L, 999L);

        mockMvc.perform(put("/usuarios/desassociar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar desassociar usuário que não possui vínculo com o restaurante (404)")
    void deveRetornarNotFoundAoDesassociarUsuarioSemVinculoComRestaurante() throws Exception {
        // Usuário 6 (Fernanda) não possui vínculo com o Restaurante 1
        DesassociarUsuarioTipoDTO dto = new DesassociarUsuarioTipoDTO(1L, 1L, 6L);

        mockMvc.perform(put("/usuarios/desassociar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Forbidden quando usuário logado não tiver permissão para desassociar (403)")
    void deveRetornarForbiddenAoDesassociarComUsuarioSemPermissao() throws Exception {
        // Usuário 5 (Cliente) tentando desassociar
        DesassociarUsuarioTipoDTO dto = new DesassociarUsuarioTipoDTO(5L, 1L, 5L);

        mockMvc.perform(put("/usuarios/desassociar-tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}
