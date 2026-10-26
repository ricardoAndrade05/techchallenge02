package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.ExcluirRestauranteDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class ExcluirRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;

    public static ExcluirRestauranteUseCase getInstance(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        return new ExcluirRestauranteUseCase(restauranteGateway, usuarioGateway);
    }

    public void execute(ExcluirRestauranteDTO excluirRestauranteDTO) {
        Usuario usuario = this.usuarioGateway.buscaUsuarioPorId(excluirRestauranteDTO.idUsuarioLogado(),excluirRestauranteDTO.idRestaurante())
            .orElseThrow(() -> new RecursoNaoEncontradoException ("Usuário não encontrado"));
        Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(excluirRestauranteDTO.idRestaurante())
            .orElseThrow(() -> new RecursoNaoEncontradoException ("Restaurante não encontrado"));
        Usuario dono = restaurante.getDono();
        if (!usuario.podeExcluir() || !dono.getId().equals(usuario.getId())) {
            throw new UsuarioSemPermissaoException("Usuário não tem permissão para excluir restaurante");
        }
        this.restauranteGateway.excluirRestaurante(excluirRestauranteDTO.idRestaurante());
       
    }

    private ExcluirRestauranteUseCase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

}
