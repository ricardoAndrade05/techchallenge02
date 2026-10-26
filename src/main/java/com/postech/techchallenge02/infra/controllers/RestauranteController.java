package com.postech.techchallenge02.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.infra.dtos.AtualizaRestauranteDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiRestauranteDTO;
import com.postech.techchallenge02.infra.services.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController implements RestauranteAPI {

    private final RestauranteService restauranteService;

    RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarPorId(@PathVariable Long id) {
        RestauranteDTO restauranteDTO = this.restauranteService.buscarPorId(id);
        return ResponseEntity.ok().body(restauranteDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> atualizarRestaurante(@PathVariable Long id, @RequestBody AtualizaRestauranteDTO dto) {
        RestauranteDTO restauranteDTO = this.restauranteService.atualizarRestaurante(id, dto);
        return ResponseEntity.ok().body(restauranteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRestaurante(@PathVariable Long id, @RequestBody ExcluiRestauranteDTO dto) {
        this.restauranteService.excluirRestaurante(id, dto.idUsuarioLogado());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<RestauranteDTO> inserirRestaurante(@RequestBody NovoRestauranteDTO dto) {
        RestauranteDTO restauranteDTO = this.restauranteService.cadastrarRestaurante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteDTO);
    }
}
