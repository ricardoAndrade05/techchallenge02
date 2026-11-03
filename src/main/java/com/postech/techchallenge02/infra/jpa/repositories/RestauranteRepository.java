package com.postech.techchallenge02.infra.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postech.techchallenge02.infra.jpa.entities.RestauranteEntity;

@Repository 
public interface RestauranteRepository extends JpaRepository<RestauranteEntity, Long> {

}
