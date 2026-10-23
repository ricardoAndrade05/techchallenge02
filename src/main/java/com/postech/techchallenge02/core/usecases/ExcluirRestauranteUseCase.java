package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.ExcluirRestauranteDTO;
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
        if (!usuario.podeExcluir()) {
            throw new UsuarioSemPermissaoException("Usuário não tem permissão para excluir restaurante");
        }
        this.restauranteGateway.excluirRestaurante(excluirRestauranteDTO.idRestaurante());
       
    }

    private ExcluirRestauranteUseCase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

}
