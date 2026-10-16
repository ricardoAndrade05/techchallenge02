package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.CategoriaEnun;

public record NovoTipoUsuarioDTO(Long idRestaurante, String nomeTipo, CategoriaEnun tipoUsuario, Long idUsuarioCriador) {

    public NovoTipoUsuarioDTO(Long idRestaurante, String nomeTipo, CategoriaEnun tipoUsuario) {
        this(idRestaurante, nomeTipo, tipoUsuario, null);
    }

}
