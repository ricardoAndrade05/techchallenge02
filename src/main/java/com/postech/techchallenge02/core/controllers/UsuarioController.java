package com.postech.techchallenge02.core.controllers;

import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.gateways.TipoUsuarioGatewayImpl;
import com.postech.techchallenge02.core.gateways.UsuarioGatewayImpl;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;
import com.postech.techchallenge02.core.presenters.UsuarioPresenter;
import com.postech.techchallenge02.core.usecases.AssociarUsuarioAoTipoUseCase;
import com.postech.techchallenge02.core.usecases.DesassociarUsuarioAoTipoUseCase;

public class UsuarioController {

    private final UsuarioDataSource usuarioDataSource;
    private final TipoUsuarioDataSource tipoUsuariodataSource;

    private UsuarioController(UsuarioDataSource usuarioDataSource, TipoUsuarioDataSource tipoUsuariodataSource) {
        this.usuarioDataSource = usuarioDataSource;
        this.tipoUsuariodataSource = tipoUsuariodataSource;
    }

    public static UsuarioController getInstance(UsuarioDataSource usuarioDataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        return new UsuarioController(usuarioDataSource, tipoUsuarioDataSource);
    }
    
    public UsuarioDTO associarUsuarioAoTipo(AssociarUsuarioAoTipoDTO dto) {
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource,this.tipoUsuariodataSource);
        TipoUsuarioGateway tipoUsuarioGateway = TipoUsuarioGatewayImpl.getInstance(this.tipoUsuariodataSource,this.usuarioDataSource);
        Usuario usuario = AssociarUsuarioAoTipoUseCase.getInstance(usuarioGateway, tipoUsuarioGateway).execute(dto);

        return UsuarioPresenter.toDTO(usuario);
    }

    public void desassociarUsuarioAoTipo(DesassociarUsuarioTipoDTO dto) {
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource,this.tipoUsuariodataSource);
        DesassociarUsuarioAoTipoUseCase.getInstance(usuarioGateway).execute(dto);
    }
        
}
