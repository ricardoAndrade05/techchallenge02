package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

public record TipoUsuarioDTO(
    Long id,
    RestauranteDTO restaurante,
    String nome,
    TipoUsuarioEnun tipo) {

}
