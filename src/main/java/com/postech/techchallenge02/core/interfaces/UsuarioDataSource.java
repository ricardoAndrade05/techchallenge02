package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;

public interface UsuarioDataSource {

    public UsuarioDonoDTO buscaUsuarioPorId(Long idUsuario, Long idRestaurante);

    public UsuarioDonoDTO buscaUsuarioPorId(Long idUsuario);

    public boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario);

    public boolean usuarioEstaVinculadoAoRestaurante(Long idUsuario, Long idRestaurante);

    public UsuarioDTO vincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario);

    public void desvincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario);

}
