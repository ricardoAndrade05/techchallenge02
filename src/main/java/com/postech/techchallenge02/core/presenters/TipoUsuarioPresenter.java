package com.postech.techchallenge02.core.presenters;

import java.util.List;
import java.util.stream.Collectors;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;

public class TipoUsuarioPresenter { 

    public static TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario) {
        return new TipoUsuarioDTO(tipoUsuario.getId(), tipoUsuario.getIdRestaurante(), tipoUsuario.getNome(), tipoUsuario.getTipo());
    }

    public static List<TipoUsuarioDTO> toDTOs(List<TipoUsuario> tiposUsuario) {
        return tiposUsuario.stream().map(TipoUsuarioPresenter::toDTO).collect(Collectors.toList());
    }

}
