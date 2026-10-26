package com.postech.techchallenge02.infra.infra.mappers;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDonoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;

@Component
public class UsuarioMapper {

    private final TipoUsuarioMapper tipoUsuarioMapper;

    UsuarioMapper(TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    public UsuarioDonoDTO toUsuarioDonoDTO(Usuario usuario, TipoUsuario tipoUsuario) {
        if (usuario == null && tipoUsuario == null) {
            return null;
        }

        Long id = null;
        String nome = null;
        if (usuario != null) {
            id = usuario.getId();
            nome = usuario.getNome();
        }
        TipoUsuarioDonoDTO tipoUsuario1 = null;
        tipoUsuario1 = tipoUsuarioMapper.toTipoUsuarioRetornoDTO(tipoUsuario);

        UsuarioDonoDTO usuarioDonoDTO = new UsuarioDonoDTO(id, nome, tipoUsuario1);

        return usuarioDonoDTO;
    }

    public UsuarioDonoDTO toUsuarioDonoDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        Long id = null;
        String nome = null;

        id = usuario.getId();
        nome = usuario.getNome();

        TipoUsuarioDonoDTO tipoUsuario = null;

        UsuarioDonoDTO usuarioDonoDTO = new UsuarioDonoDTO(id, nome, tipoUsuario);

        return usuarioDonoDTO;
    }

    public UsuarioDTO toDTO(Usuario usuario, TipoUsuario tipoUsuario) {
        if (usuario == null && tipoUsuario == null) {
            return null;
        }

        Long id = null;
        String nome = null;
        if (usuario != null) {
            id = usuario.getId();
            nome = usuario.getNome();
        }
        TipoUsuarioDTO tipoUsuario1 = null;
        tipoUsuario1 = tipoUsuarioMapper.toDTO(tipoUsuario);

        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, tipoUsuario1);

        return usuarioDTO;
    }

}
