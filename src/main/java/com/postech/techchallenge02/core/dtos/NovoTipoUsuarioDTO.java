package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

public record NovoTipoUsuarioDTO(Long idRestaurante, String nomeTipo, TipoUsuarioEnun tipoUsuario, Long idUsuarioCriador) {

    public NovoTipoUsuarioDTO(Long idRestaurante, String nomeTipo, TipoUsuarioEnun tipoUsuario) {
        this(idRestaurante, nomeTipo, tipoUsuario, null);
    }

}
