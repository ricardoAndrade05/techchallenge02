package com.postech.techchallenge02.infra.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.postech.techchallenge02.infra.jpa.entities.ItemCardapioEntity;

@Repository 
public interface ItemCardapioRepository extends JpaRepository<ItemCardapioEntity, Long> {

	@Query("SELECT COUNT(ic) > 0 "
			+ "FROM ItemCardapioEntity ic "
			+ "WHERE ic.restaurante.id = :idRestaurante "
			+ "AND ic.nome = :nome")
	boolean existeItemCardapio(String nome, Long idRestaurante);

	@Query("SELECT COUNT(ic) > 0 "
			+ "FROM ItemCardapioEntity ic "
			+ "WHERE ic.id = :idItemCardapio "
			+ "AND ic.restaurante.id = :idRestaurante ")
	boolean existeItemCardapio(Long idItemCardapio, Long idRestaurante);

	Page<ItemCardapioEntity> findByRestauranteId(Long idRestaurante, Pageable pageable);

	boolean existsByIdAndRestauranteId(Long id, Long idRestaurante);
	boolean existsByNomeAndRestauranteId(String nome, Long idRestaurante);

}
