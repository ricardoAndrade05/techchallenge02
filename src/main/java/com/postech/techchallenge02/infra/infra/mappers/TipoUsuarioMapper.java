package com.postech.techchallenge02.infra.infra.mappers;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDonoDTO;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.infra.entities.TipoUsuario;

@Component 
public class TipoUsuarioMapper {

    private final RestauranteMapper restauranteMapper;

    TipoUsuarioMapper(RestauranteMapper restauranteMapper) {
        this.restauranteMapper = restauranteMapper;
    }


    public TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario) {
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


    public TipoUsuario toEntity(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        if ( novoTipoUsuarioDTO == null ) {
            return null;
        }

        TipoUsuario tipoUsuario = new TipoUsuario();

        tipoUsuario.setTipo( novoTipoUsuarioDTO.tipoUsuario() );
        tipoUsuario.setNome( novoTipoUsuarioDTO.nomeTipo() );

        return tipoUsuario;
    }


    public TipoUsuarioDonoDTO toTipoUsuarioRetornoDTO(TipoUsuario tipoUsuario) {
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
