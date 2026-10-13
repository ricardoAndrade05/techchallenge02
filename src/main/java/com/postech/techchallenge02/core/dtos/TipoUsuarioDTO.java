package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

public record TipoUsuarioDTO(
    Long id,
    Long idRestaurante,
    String nome,
    TipoUsuarioEnun tipoUsuario) {

}
