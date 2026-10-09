package com.postech.techchallenge02.core.gateways;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class TipoUsuarioGatewayImpl implements TipoUsuarioGateway {

    private final TipoUsuarioDataSource dataSource;
    private final RestauranteDataSource restauranteDataSource;

    private TipoUsuarioGatewayImpl(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource) {
        this.dataSource = dataSource;
        this.restauranteDataSource = restauranteDataSource;
    }

    public static TipoUsuarioGateway getInstance(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource) {
        if (dataSource == null || restauranteDataSource == null) {
            throw new IllegalArgumentException("Os data sources não podem ser nulos");
        }
        return new TipoUsuarioGatewayImpl(dataSource, restauranteDataSource);
    }

    @Override
    public TipoUsuario atualizarTipoUsuario(TipoUsuario tipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarTipoUsuario'");
    }

@Override
public Optional<TipoUsuario> buscaTipoUsuarioPorId(Long idTipoUsuario) {
    TipoUsuarioDTO tipoUsuarioDTO = this.dataSource.buscaTipoUsuarioPorId(idTipoUsuario);
    if (tipoUsuarioDTO == null) {
        return Optional.empty();
    }

    RestauranteDTO restauranteDTO = this.restauranteDataSource.buscaRestaurantePorId(tipoUsuarioDTO.idRestaurante());
    Restaurante restaurante = (restauranteDTO != null) 
        ? Restaurante.restaurar(restauranteDTO.id(), restauranteDTO.nome()) 
        : null;

    TipoUsuario tipoUsuario = TipoUsuario.restaurar(
        idTipoUsuario,
        restaurante,
        tipoUsuarioDTO.nome(),
        tipoUsuarioDTO.tipoUsuario()
    );

    return Optional.of(tipoUsuario);
}


    @Override
    public Pagina<TipoUsuario> buscaTipoUsuarioPorRestaurante(Restaurante restaurante, Paginacao paginacao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscaTipoUsuarioPorRestaurante'");
    }

    @Override
    public TipoUsuario cadastraTipoUsuario(TipoUsuario novoTipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cadastraTipoUsuario'");
    }

    @Override
    public boolean tipoUsuarioEstaVinculado(Long idTipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tipoUsuarioEstaVinculado'");
    }

    @Override
    public boolean excluirTipoUsuario(Long idTipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'excluirTipoUsuario'");
    }

    @Override
    public boolean existeTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeTipoUsuario'");
    }

    @Override
    public boolean existeTipoUsuario(Long idTipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeTipoUsuario'");
    }

}
