package com.postech.techchallenge02.core.presenters;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;

public class UsuarioPresenter {
    
    
    public static UsuarioDTO toDTO(Usuario usuario) {
        TipoUsuario tipoUsuario = usuario.getTipoUsuario();
        TipoUsuarioDTO tipoUsuarioDTO = new TipoUsuarioDTO(
                tipoUsuario.getId(),
                null,
                tipoUsuario.getNome(),
                tipoUsuario.getCategoria());

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                tipoUsuarioDTO);
    }
}
