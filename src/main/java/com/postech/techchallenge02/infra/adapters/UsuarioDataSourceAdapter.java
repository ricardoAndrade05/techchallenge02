package com.postech.techchallenge02.infra.adapters;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;

public class UsuarioDataSourceAdapter implements UsuarioDataSource {

    public UsuarioDataSourceAdapter() {

    }

    @Override
    public UsuarioDTO buscaUsuarioPorId(Long idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscaUsuarioPorId'");
    }

    @Override
    public boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeUsuarioComEsteTipoUsuario'");
    }

}
