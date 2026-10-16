package com.postech.techchallenge02.core.presenters;

import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.Usuario;

public class RestaurantePresenter {

    public static RestauranteDTO toDTO(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }

        return new RestauranteDTO(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getTipoCozinha(),
            formatarHorarioFuncionamento(restaurante),
            toEnderecoDTO(restaurante.getEndereco()),
            toUsuarioDTO(restaurante.getDono())
        );
    }

    private static String formatarHorarioFuncionamento(Restaurante restaurante) {
        if (restaurante.getHorarioAbertura() == null || restaurante.getHorarioFechamento() == null) {
            return null;
        }
        return restaurante.getHorarioAbertura() + " - " + restaurante.getHorarioFechamento();
    }

    private static EnderecoDTO toEnderecoDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return new EnderecoDTO(
            endereco.getId(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }

    private static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            null
        );
    }
}