package com.postech.techchallenge02.infra.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.infra.jpa.entities.TipoUsuarioEntity;

@Repository 
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, Long> {

    @Query("SELECT COUNT(tu) > 0 "
        +  "FROM TipoUsuarioEntity tu "
        +  "WHERE tu.restaurante.id = :idRestaurante "
        +  "AND tu.tipo = :tipoUsuarioEnun "
        +  "AND tu.nome = :nome")
    boolean existeTipoUsuario(String nome ,Long idRestaurante, CategoriaEnun tipoUsuarioEnun);


    @Query("SELECT COUNT(tu) > 0 "
        +  "FROM TipoUsuarioEntity tu "
        +  "WHERE tu.id = :idTipoUsuario "
        +  "AND tu.restaurante.id = :idRestaurante ")
    boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante);

    @Query("SELECT tu FROM TipoUsuarioEntity tu WHERE tu.restaurante.id = :idRestaurante")
    Page<TipoUsuarioEntity> buscaTiposUsuarioPorRestaurante(@Param("idRestaurante") Long idRestaurante, Pageable pageable);

}
