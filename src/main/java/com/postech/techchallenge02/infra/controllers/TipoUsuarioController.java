package com.postech.techchallenge02.infra.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.infra.services.TipoUsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tipo-usuario")
@Tag(name = "Tipo usuario", description = "Endpoints para gerenciamento de tipo usuario")
public class TipoUsuarioController implements TipoUsuarioAPI {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> buscaTipoUsuarioPorId(@PathVariable Long id) {
        TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(tipoUsuarioDTO);
    }

    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<Page<TipoUsuarioDTO>> buscaTiposUsuarioRestaurante(@PathVariable Long idRestaurante, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TipoUsuarioDTO> tiposUsuarioDTO = this.tipoUsuarioService.buscaTiposUsuarioRestaurante(idRestaurante, pageable);
        return ResponseEntity.ok().body(tiposUsuarioDTO);
    }

    @PostMapping
    public ResponseEntity<TipoUsuarioDTO> inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
       TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioService.inserirTipoUsuario(novoTipoUsuarioDTO);
       URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tipoUsuarioDTO.id()).toUri();
       return ResponseEntity.created(uri).body(tipoUsuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTipoUsuario(ExcluirTipoUsuarioDTO excluirTipoUsuarioDTO) {
        this.tipoUsuarioService.excluiTipoUsuario(excluirTipoUsuarioDTO);
        return ResponseEntity.noContent().build();
    }

   @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> atualizarTipoUsuario(AtualizarTipoUsuarioDTO atualizarTipoUsuarioDTO) {
       TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioService.atualizaTipoUsuario(atualizarTipoUsuarioDTO);
       return ResponseEntity.ok().body(tipoUsuarioDTO);
    }

}