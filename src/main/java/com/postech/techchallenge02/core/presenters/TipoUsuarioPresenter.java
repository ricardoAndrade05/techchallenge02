package com.postech.techchallenge02.core.presenters;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;

public class TipoUsuarioPresenter { 

    public static TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario) {
        return new TipoUsuarioDTO(tipoUsuario.getId(), tipoUsuario.getIdRestaurante(), tipoUsuario.getNome(), tipoUsuario.getTipo());
    }

}
