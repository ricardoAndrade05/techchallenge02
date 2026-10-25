package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import com.postech.techchallenge02.core.entities.Usuario;

public interface UsuarioGateway {

    Optional<Usuario> buscaUsuarioPorId(Long idUsuario,Long idRestaurante);

    Optional<Usuario> buscaUsuarioPorId(Long idUsuarioLogado);

    Usuario vincularUsuarioAoTipo(Long idUsuario,Long idTipoUsuario);

    boolean usuarioVinculadoAoRestaurante(Long idUsuario,Long idRestaurante);

    void desvincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario);

}
