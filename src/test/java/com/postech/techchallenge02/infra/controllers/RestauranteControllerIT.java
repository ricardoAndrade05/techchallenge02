package com.postech.techchallenge02.infra.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;

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
import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.infra.dtos.AtualizaRestauranteDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiRestauranteDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/massa-testes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Testes de Integração - RestauranteController")
public class RestauranteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- GET /restaurantes/{id} ---

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso (200)")
    void deveBuscarPorIdComSucesso() throws Exception {
        Long idExistente = 1L; // Trattoria Bella Italia

        mockMvc.perform(get("/restaurantes/{id}", idExistente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idExistente))
                .andExpect(jsonPath("$.nome").value("Trattoria Bella Italia"))
                .andExpect(jsonPath("$.tipoCozinha").value("ITALIANA"))
                .andExpect(jsonPath("$.endereco").exists())
                .andExpect(jsonPath("$.dono").exists());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao buscar restaurante inexistente (404)")
    void deveRetornarNotFoundQuandoRestauranteNaoExistir() throws Exception {
        Long idInexistente = 999L;

        mockMvc.perform(get("/restaurantes/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }

    // --- POST /restaurantes ---

    @Test
    @DisplayName("Deve inserir novo restaurante com sucesso (201)")
    void deveInserirRestauranteComSucesso() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO(null, "Rua Oscar Freire", "100", "São Paulo", "SP", "01426-000");
        NovoRestauranteDTO novoDTO = new NovoRestauranteDTO(
                "Novo Bistrô Francês",
                enderecoDTO,
                2, // ITALIANA
                LocalTime.of(12, 0),
                LocalTime.of(23, 0),
                1L // Carlos Silva
        );

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Novo Bistrô Francês"))
                .andExpect(jsonPath("$.endereco.logradouro").value("Rua Oscar Freire"));
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar inserir restaurante com usuário criador inexistente (404)")
    void deveRetornarNotFoundAoInserirComUsuarioInexistente() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO(null, "Rua Oscar Freire", "100", "São Paulo", "SP", "01426-000");
        NovoRestauranteDTO novoDTO = new NovoRestauranteDTO(
                "Novo Bistrô",
                enderecoDTO,
                2,
                LocalTime.of(12, 0),
                LocalTime.of(23, 0),
                999L // Usuário inexistente
        );

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Bad Request ao tentar inserir restaurante com nome em branco (400)")
    void deveRetornarBadRequestAoInserirComNomeEmBranco() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO(null, "Rua Oscar Freire", "100", "São Paulo", "SP", "01426-000");
        NovoRestauranteDTO novoDTO = new NovoRestauranteDTO(
                "   ",
                enderecoDTO,
                2,
                LocalTime.of(12, 0),
                LocalTime.of(23, 0),
                1L
        );

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar Bad Request ao tentar inserir restaurante com horário de abertura posterior ao fechamento (400)")
    void deveRetornarBadRequestAoInserirComHorariosInvalidos() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO(null, "Rua Oscar Freire", "100", "São Paulo", "SP", "01426-000");
        NovoRestauranteDTO novoDTO = new NovoRestauranteDTO(
                "Bistrô Invalido",
                enderecoDTO,
                2,
                LocalTime.of(23, 0), // Abertura após fechamento
                LocalTime.of(11, 0),
                1L
        );

        mockMvc.perform(post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoDTO)))
                .andExpect(status().isBadRequest());
    }

    // --- PUT /restaurantes/{id} ---

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso quando usuário for o dono (200)")
    void deveAtualizarRestauranteComSucesso() throws Exception {
        Long idRestaurante = 1L; // Trattoria Bella Italia (dono: Usuário 1)
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Trattoria Bella Italia Atualizada",
                "Rua Haddock Lobo",
                "1300",
                "São Paulo",
                "SP",
                "01414-002",
                2,
                LocalTime.of(12, 0),
                LocalTime.of(23, 30),
                null, // mantem o mesmo dono
                1L // Carlos Silva (dono)
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idRestaurante))
                .andExpect(jsonPath("$.nome").value("Trattoria Bella Italia Atualizada"))
                .andExpect(jsonPath("$.horarioInicio").value("12:00:00"))
                .andExpect(jsonPath("$.horarioFim").value("23:30:00"));
    }

    @Test
    @DisplayName("Deve atualizar restaurante e transferir propriedade com sucesso (200)")
    void deveAtualizarRestauranteComTrocaDeDonoComSucesso() throws Exception {
        Long idRestaurante = 1L; // Trattoria Bella Italia (dono atual: Usuário 1)
        Long idNovoDono = 2L; // Ana Oliveira

        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Trattoria Bella Italia",
                "Rua Haddock Lobo",
                "1300",
                "São Paulo",
                "SP",
                "01414-002",
                2,
                LocalTime.of(11, 30),
                LocalTime.of(23, 0),
                idNovoDono,
                1L // Carlos Silva
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dono.id").value(idNovoDono));
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar atualizar restaurante inexistente (404)")
    void deveRetornarNotFoundAoAtualizarRestauranteInexistente() throws Exception {
        Long idInexistente = 999L;
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Novo Nome", "Rua A", "1", "SP", "SP", "01000-000",
                2, LocalTime.of(11, 0), LocalTime.of(22, 0), null, 1L
        );

        mockMvc.perform(put("/restaurantes/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar atualizar com usuário logado inexistente (404)")
    void deveRetornarNotFoundAoAtualizarComUsuarioLogadoInexistente() throws Exception {
        Long idRestaurante = 1L;
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Novo Nome", "Rua A", "1", "SP", "SP", "01000-000",
                2, LocalTime.of(11, 0), LocalTime.of(22, 0), null, 999L // usuário inexistente
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar transferir dono para usuário inexistente (404)")
    void deveRetornarNotFoundAoAtualizarComNovoDonoInexistente() throws Exception {
        Long idRestaurante = 1L;
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Trattoria Bella Italia", "Rua Haddock Lobo", "1300", "São Paulo", "SP", "01414-002",
                2, LocalTime.of(11, 30), LocalTime.of(23, 0), 999L, 1L // novo dono inexistente
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Forbidden ao tentar atualizar restaurante sem ser o dono (403)")
    void deveRetornarForbiddenAoAtualizarSemSerODono() throws Exception {
        Long idRestaurante = 1L; // Dono é Usuário 1
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Nome Alterado", "Rua Haddock Lobo", "1300", "São Paulo", "SP", "01414-002",
                2, LocalTime.of(11, 30), LocalTime.of(23, 0), null, 5L // Usuário 5 (não é dono do Rest 1)
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar Forbidden ao tentar atualizar restaurante com usuário tipo CLIENTE (403)")
    void deveRetornarForbiddenAoAtualizarComUsuarioCliente() throws Exception {
        Long idRestaurante = 1L;
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Nome Alterado", "Rua Haddock Lobo", "1300", "São Paulo", "SP", "01414-002",
                2, LocalTime.of(11, 30), LocalTime.of(23, 0), null, 5L // Usuário 5 (Cliente)
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar Bad Request ao tentar atualizar com horários de funcionamento inválidos (400)")
    void deveRetornarBadRequestAoAtualizarComHorariosInvalidos() throws Exception {
        Long idRestaurante = 1L;
        AtualizaRestauranteDTO dto = new AtualizaRestauranteDTO(
                "Trattoria", "Rua Haddock Lobo", "1300", "São Paulo", "SP", "01414-002",
                2, LocalTime.of(23, 0), LocalTime.of(10, 0), null, 1L
        );

        mockMvc.perform(put("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE /restaurantes/{id} ---

    @Test
    @DisplayName("Deve excluir restaurante com sucesso quando solicitado pelo dono (204)")
    void deveExcluirRestauranteComSucesso() throws Exception {
        Long idRestaurante = 1L; // Dono é Usuário 1
        ExcluiRestauranteDTO dto = new ExcluiRestauranteDTO(1L);

        mockMvc.perform(delete("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        // Confirma que foi excluído
        mockMvc.perform(get("/restaurantes/{id}", idRestaurante))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar excluir restaurante inexistente (404)")
    void deveRetornarNotFoundAoExcluirRestauranteInexistente() throws Exception {
        Long idInexistente = 999L;
        ExcluiRestauranteDTO dto = new ExcluiRestauranteDTO(1L);

        mockMvc.perform(delete("/restaurantes/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar excluir com usuário inexistente (404)")
    void deveRetornarNotFoundAoExcluirComUsuarioInexistente() throws Exception {
        Long idRestaurante = 1L;
        ExcluiRestauranteDTO dto = new ExcluiRestauranteDTO(999L);

        mockMvc.perform(delete("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Forbidden ao tentar excluir restaurante sem ser o dono (403)")
    void deveRetornarForbiddenAoExcluirSemSerODono() throws Exception {
        Long idRestaurante = 1L;
        ExcluiRestauranteDTO dto = new ExcluiRestauranteDTO(5L); // Usuário 5 não é dono do Rest 1

        mockMvc.perform(delete("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar Forbidden ao tentar excluir restaurante com usuário tipo CLIENTE (403)")
    void deveRetornarForbiddenAoExcluirComUsuarioCliente() throws Exception {
        Long idRestaurante = 1L;
        ExcluiRestauranteDTO dto = new ExcluiRestauranteDTO(5L); // Usuário 5 é Cliente

        mockMvc.perform(delete("/restaurantes/{id}", idRestaurante)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}
