package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;

public interface TipoUsuarioDataSource {

    TipoUsuarioDTO buscaTipoUsuarioPorId(Long idTipoUsuario);

    TipoUsuarioDTO cadastraTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO);

    boolean excluirTipoUsuario(Long idTipoUsuario);

    TipoUsuarioDTO atualizar(TipoUsuarioDTO dto);
    

}
