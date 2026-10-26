package com.postech.techchallenge02.core.dtos;

public record UsuarioDonoDTO(
        Long id,
        String nome,
        TipoUsuarioDonoDTO tipoUsuario) {
}
