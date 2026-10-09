package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

public record NovoTipoUsuarioDTO(Restaurante restaurante,String nomeTipo,TipoUsuarioEnun tipoUsuario) {

}
