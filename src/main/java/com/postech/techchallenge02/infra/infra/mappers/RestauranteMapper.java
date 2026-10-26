package com.postech.techchallenge02.infra.infra.mappers;

import java.time.LocalTime;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDonoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.infra.entities.Restaurante;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;

@Component
public class RestauranteMapper {

    private final EnderecoMapper enderecoMapper;

    RestauranteMapper(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }

    public RestauranteDTO toDTO(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }

        LocalTime horarioInicio = null;
        LocalTime horarioFim = null;
        Long id = null;
        String nome = null;
        TipoCozinhaEnun tipoCozinha = null;

        horarioInicio = restaurante.getHorarioFuncionamentoInicio();
        horarioFim = restaurante.getHorarioFuncionamentoFim();
        id = restaurante.getId();
        nome = restaurante.getNome();
        tipoCozinha = restaurante.getTipoCozinha();

        EnderecoDTO endereco = this.enderecoMapper.toDTO(restaurante.getEndereco());

        UsuarioDonoDTO dono = this.usuarioDonoDTO(restaurante.getDono(), restaurante.getId());

        RestauranteDTO restauranteDTO = new RestauranteDTO(id, nome, tipoCozinha, horarioInicio, horarioFim, endereco, dono);

        return restauranteDTO;
    }

    public Restaurante toEntity(NovoRestauranteDTO dto) {
        if (dto == null) {
            return null;
        }

        Restaurante restaurante = new Restaurante();

        restaurante.setHorarioFuncionamentoInicio(dto.horarioAbertura());
        restaurante.setHorarioFuncionamentoFim(dto.horarioFechamento());
        restaurante.setTipoCozinha(TipoCozinhaEnun.fromCodigo(dto.tipoCozinha()));
        restaurante.setNome(dto.nome());
        restaurante.setEndereco(enderecoMapper.toEntity(dto.endereco()));

        return restaurante;
    }


    public Restaurante toEntity(AtualizarRestauranteDTO dto) {
        if (dto == null) {
            return null;
        }

        Restaurante restaurante = new Restaurante();
        restaurante.setId(dto.idRestaurante());
        restaurante.setHorarioFuncionamentoInicio(dto.horaAbertura());
        restaurante.setHorarioFuncionamentoFim(dto.horaFechamento());
        restaurante.setTipoCozinha(dto.tipoCozinha());
        restaurante.setNome(dto.nome());
        restaurante.setEndereco(enderecoMapper.toEntity(dto.endereco()));

        return restaurante;
    }

    private UsuarioDonoDTO usuarioDonoDTO(Usuario usuario, Long idRestaurante) {
        if (usuario == null) {
            return null;
        }

        TipoUsuarioDonoDTO tipoUsuario = null;
        Set<TipoUsuario> tipos = usuario.getTiposUsuario();

        if (tipos != null && !tipos.isEmpty() && idRestaurante != null) {
            for (TipoUsuario tu : tipos) {
                Restaurante restaurante = tu.getRestaurante();
                if (restaurante != null && idRestaurante.equals(restaurante.getId())) {
                    tipoUsuario = new TipoUsuarioDonoDTO(tu.getId(), tu.getNome(), tu.getTipo());
                    break;
                }
            }
        }

        return new UsuarioDonoDTO(usuario.getId(), usuario.getNome(), tipoUsuario);
    }

}
