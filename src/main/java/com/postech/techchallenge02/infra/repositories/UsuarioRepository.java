package com.postech.techchallenge02.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postech.techchallenge02.infra.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   
    @Query("SELECT COUNT(u) > 0 "
            + "FROM Usuario u JOIN u.tiposUsuario tu WHERE tu.id = :idTipoUsuario")
    boolean existeUsuarioComEsteTipoUsuario(@Param("idTipoUsuario") Long idTipoUsuario);
}
