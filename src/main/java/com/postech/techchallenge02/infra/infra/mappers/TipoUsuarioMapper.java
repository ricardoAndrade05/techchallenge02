package com.postech.techchallenge02.infra.infra.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.infra.entities.TipoUsuario;

@Mapper(componentModel = "spring", uses = {RestauranteMapper.class})
public interface TipoUsuarioMapper {

    TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    @Mapping(source = "tipoUsuario", target = "tipo")
    @Mapping(source = "nomeTipo", target = "nome")
    TipoUsuario toEntity(NovoTipoUsuarioDTO novoTipoUsuarioDTO);

}
