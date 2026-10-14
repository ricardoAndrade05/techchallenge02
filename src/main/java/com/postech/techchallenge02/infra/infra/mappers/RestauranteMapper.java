package com.postech.techchallenge02.infra.infra.mappers;

import org.mapstruct.Mapper;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.infra.entities.Restaurante;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {

    RestauranteDTO toDTO(Restaurante restaurante);

}
