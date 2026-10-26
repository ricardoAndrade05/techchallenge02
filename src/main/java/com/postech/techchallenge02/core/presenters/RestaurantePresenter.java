package com.postech.techchallenge02.core.presenters;

import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDonoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
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
            restaurante.getHorarioAbertura(),
            restaurante.getHorarioFechamento(),
            toEnderecoDTO(restaurante.getEndereco()),
            toUsuarioDTO(restaurante.getDono())
        );
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

    private static UsuarioDonoDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        TipoUsuarioDonoDTO tipoUsuarioDonoDTO = null;
        TipoUsuario tipoUsuario = usuario.getTipoUsuario();
        if (tipoUsuario != null) {
             tipoUsuarioDonoDTO = new TipoUsuarioDonoDTO(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getCategoria()
             );
        }
        return new UsuarioDonoDTO(
            usuario.getId(),
            usuario.getNome(),
            tipoUsuarioDonoDTO
        );
    }
}