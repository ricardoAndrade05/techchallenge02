package com.postech.techchallenge02.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.postech.techchallenge02.infra.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>     {
    @Query("SELECT COUNT(u) > 0 "
        +  "FROM Usuario u "
        +  "WHERE u.tipoUsuario.id = :idTipoUsuario")
    boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario);
    
}
