package com.postech.techchallenge02.infra.infra.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.infra.entities.Restaurante;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {

    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "dono", ignore = true)
    @Mapping(target = "horarioFuncionamento", ignore = true)    
    @Mapping(target = "tipoCozinha", ignore = true)
    RestauranteDTO toDTO(Restaurante restaurante);

}
