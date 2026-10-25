package com.postech.techchallenge02.infra.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.techchallenge02.core.controllers.UsuarioController;
import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;

@Service
public class UsuarioService {

    private final UsuarioController usuarioController;

    public UsuarioService(UsuarioDataSource usuarioDataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        this.usuarioController = UsuarioController.getInstance(usuarioDataSource, tipoUsuarioDataSource);
    }

    @Transactional
    public UsuarioDTO associarUsuarioAoTipo(AssociarUsuarioAoTipoDTO dto) {
        return usuarioController.associarUsuarioAoTipo(dto);
    }

    @Transactional
    public void desassociarUsuarioAoTipoUsuario(DesassociarUsuarioTipoDTO dto) {
        usuarioController.desassociarUsuarioAoTipo(dto);
    }
}