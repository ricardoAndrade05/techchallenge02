package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import com.postech.techchallenge02.core.entities.Usuario;

public interface UsuarioGateway {

    Optional<Usuario> buscaUsuarioPorId(Long id);

}
