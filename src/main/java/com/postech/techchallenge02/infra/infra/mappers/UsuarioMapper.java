package com.postech.techchallenge02.infra.infra.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;

@Mapper(componentModel = "spring", uses = {TipoUsuarioMapper.class})
public interface UsuarioMapper {

    @Mapping(target = "id", source = "usuario.id")
    @Mapping(target = "nome", source = "usuario.nome")
    @Mapping(target = "idTipoUsuario", source = "tipoUsuario.id")
    UsuarioDonoDTO toDTO(Usuario usuario, TipoUsuario tipoUsuario);

}
