package com.postech.techchallenge02.infra.infra.mappers;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDonoDTO;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.infra.jpa.entities.TipoUsuarioEntity;

@Component 
public class TipoUsuarioMapper {

    private final RestauranteMapper restauranteMapper;

    public TipoUsuarioMapper(RestauranteMapper restauranteMapper) {
        this.restauranteMapper = restauranteMapper;
    }

    public TipoUsuarioDTO toDTO(TipoUsuarioEntity tipoUsuario) {
        if ( tipoUsuario == null ) {
            return null;
        }

        Long id = null;
        RestauranteDTO restaurante = null;
        String nome = null;
        CategoriaEnun tipo = null;

        id = tipoUsuario.getId();
        restaurante = restauranteMapper.toDTO( tipoUsuario.getRestaurante() );
        nome = tipoUsuario.getNome();
        tipo = tipoUsuario.getTipo();

        TipoUsuarioDTO tipoUsuarioDTO = new TipoUsuarioDTO( id, restaurante, nome, tipo );

        return tipoUsuarioDTO;
    }


    public TipoUsuarioEntity toEntity(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        if ( novoTipoUsuarioDTO == null ) {
            return null;
        }

        TipoUsuarioEntity tipoUsuario = new TipoUsuarioEntity();

        tipoUsuario.setTipo( novoTipoUsuarioDTO.tipoUsuario() );
        tipoUsuario.setNome( novoTipoUsuarioDTO.nomeTipo() );

        return tipoUsuario;
    }


    public TipoUsuarioDonoDTO toTipoUsuarioRetornoDTO(TipoUsuarioEntity tipoUsuario) {
        if ( tipoUsuario == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        CategoriaEnun tipo = null;

        id = tipoUsuario.getId();
        nome = tipoUsuario.getNome();
        tipo = tipoUsuario.getTipo();

        TipoUsuarioDonoDTO tipoUsuarioDonoDTO = new TipoUsuarioDonoDTO( id, nome, tipo );

        return tipoUsuarioDonoDTO;
    }

}
