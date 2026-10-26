package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.CategoriaEnun;

public record TipoUsuarioDonoDTO(
    Long id,
    String nome,
    CategoriaEnun tipo) {

}
