package com.postech.techchallenge02.core.gateways;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class RestauranteGatewayImpl implements RestauranteGateway  {

    private final RestauranteDataSource dataSource;

    private RestauranteGatewayImpl(RestauranteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RestauranteGateway getInstance(RestauranteDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("O data source não pode ser nulo");
        }
        return new RestauranteGatewayImpl(dataSource);
    }

    @Override
    public Optional<Restaurante> buscaRestaurantePorId(Long idRestaurante) {
        RestauranteDTO restauranteDTO = this.dataSource.buscaRestaurantePorId(idRestaurante);
        if (restauranteDTO == null) {
            return Optional.empty();
        }
        Restaurante restaurante = Restaurante.restaurar(restauranteDTO.id(), restauranteDTO.nome(),null,null,null,null,null);
        return Optional.of(restaurante);
    }

    @Override
    public void excluirRestaurante(Long idRestaurante) {
        this.dataSource.excluirRestaurante(idRestaurante);
    }

    @Override
    public Restaurante cadastrar(Restaurante restaurante) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cadastrar'");
    }

    @Override
    public Restaurante atualizar(Restaurante restaurante) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

}
