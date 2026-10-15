package com.postech.techchallenge02.infra.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.infra.entities.TipoUsuario;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {

    @Query("SELECT COUNT(tu) > 0 "
        +  "FROM TipoUsuario tu "
        +  "WHERE tu.restaurante.id = :idRestaurante "
        +  "AND tu.tipo = :tipoUsuarioEnun "
        +  "AND tu.nome = :nome")
    boolean existeTipoUsuario(String nome ,Long idRestaurante, TipoUsuarioEnun tipoUsuarioEnun);


    @Query("SELECT COUNT(tu) > 0 "
        +  "FROM TipoUsuario tu "
        +  "WHERE tu.id = :idTipoUsuario "
        +  "AND tu.restaurante.id = :idRestaurante ")
    boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante);

    @Query("SELECT tu FROM TipoUsuario tu WHERE tu.restaurante.id = :idRestaurante")
    Page<TipoUsuario> buscaTiposUsuarioPorRestaurante(@Param("idRestaurante") Long idRestaurante, Pageable pageable);

}
