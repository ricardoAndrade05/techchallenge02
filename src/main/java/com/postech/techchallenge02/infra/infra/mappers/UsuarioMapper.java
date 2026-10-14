package com.postech.techchallenge02.infra.infra.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.infra.entities.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "tipoUsuario.id", target = "idTipoUsuario")
    UsuarioDTO toDTO(Usuario usuario);

}
