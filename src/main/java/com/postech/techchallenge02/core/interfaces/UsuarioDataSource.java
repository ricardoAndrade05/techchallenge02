package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;

public interface UsuarioDataSource {

    public UsuarioDTO buscaUsuarioPorId(Long idUsuario);

}
