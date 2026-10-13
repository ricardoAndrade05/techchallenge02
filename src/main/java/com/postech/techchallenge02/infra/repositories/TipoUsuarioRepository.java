package com.postech.techchallenge02.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postech.techchallenge02.infra.entities.TipoUsuario;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {

}
