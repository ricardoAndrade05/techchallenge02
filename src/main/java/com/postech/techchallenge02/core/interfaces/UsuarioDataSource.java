package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;

public interface UsuarioDataSource {

    public UsuarioDonoDTO buscaUsuarioPorId(Long idUsuario, Long idRestaurante);

    public UsuarioDonoDTO buscaUsuarioPorId(Long idUsuario);

    public boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario);

}
