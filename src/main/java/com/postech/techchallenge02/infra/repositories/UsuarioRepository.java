package com.postech.techchallenge02.infra.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT COUNT(u) > 0 "
            + "FROM Usuario u JOIN u.tiposUsuario tu WHERE tu.id = :idTipoUsuario")
    boolean existeUsuarioComEsteTipoUsuario(@Param("idTipoUsuario") Long idTipoUsuario);

    @Query("SELECT COUNT(u) > 0 "
            + "FROM Usuario u JOIN u.tiposUsuario tu "
            + "WHERE u.id = :idUsuario AND tu.restaurante.id = :idRestaurante")
    boolean usuarioEstaVinculadoAoRestaurante(@Param("idUsuario") Long idUsuario, @Param("idRestaurante") Long idRestaurante);

    List<Usuario> findAllByTiposUsuarioIn(List<TipoUsuario> tiposDoRestaurante);

}
