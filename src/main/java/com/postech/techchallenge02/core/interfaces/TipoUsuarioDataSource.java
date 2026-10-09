package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;

public interface TipoUsuarioDataSource {

    TipoUsuarioDTO buscaTipoUsuarioPorId(Long idTipoUsuario);

}
