package com.postech.techchallenge02.infra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.infra.services.UsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para gerenciamento de usuarios")
public class UsuarioController implements UsuarioAPI {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping("/associar-tipo")
    public ResponseEntity<UsuarioDTO> associarUsuarioAoTipo(@RequestBody AssociarUsuarioAoTipoDTO dto) {
        UsuarioDTO usuarioDTO = this.usuarioService.associarUsuarioAoTipo(dto);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/desassociar-tipo")
    public ResponseEntity<Void> desassociarUsuarioAoTipoUsuario(@RequestBody DesassociarUsuarioTipoDTO dto) {
        this.usuarioService.desassociarUsuarioAoTipoUsuario(dto);
        return ResponseEntity.noContent().build();
    }

}
