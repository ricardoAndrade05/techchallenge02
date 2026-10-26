package com.postech.techchallenge02.infra.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.techchallenge02.core.controllers.RestauranteController;
import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.ExcluirRestauranteDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.infra.dtos.AtualizaRestauranteDTO;

@Service
public class RestauranteService {

    private final RestauranteController restauranteController;

    public RestauranteService(TipoUsuarioDataSource tipoUsuariodataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        this.restauranteController = RestauranteController.getInstance(tipoUsuariodataSource, restauranteDataSource, usuarioDataSource);
    }

    @Transactional(readOnly = true)
    public RestauranteDTO buscarPorId(Long idRestaurante) {
        return this.restauranteController.buscarPorId(idRestaurante);
    }

    @Transactional
    public RestauranteDTO atualizarRestaurante(Long idRestaurante, AtualizaRestauranteDTO dto) {
        EnderecoDTO enderecoDTO = new EnderecoDTO(null, dto.logradouro(), dto.numero(), dto.cidade(), dto.estado(), dto.cep());
        AtualizarRestauranteDTO atualizarRestauranteDTO = new AtualizarRestauranteDTO(idRestaurante, dto.nome(),
                enderecoDTO, TipoCozinhaEnun.fromCodigo(dto.tipoCozinha()), dto.inicioFuncionamento(), dto.fimFuncionamento(), dto.idDonoRestaurante(), dto.idUsuarioLogado());
        return this.restauranteController.atualizarRestaurante(atualizarRestauranteDTO);
    }

    @Transactional
    public void excluirRestaurante(Long idRestaurante, Long idUsuarioLogado) {
        this.restauranteController.excluirRestaurante(new ExcluirRestauranteDTO(idUsuarioLogado, idRestaurante));
    }

    @Transactional
    public RestauranteDTO cadastrarRestaurante(NovoRestauranteDTO novoRestauranteDTO) {
        return this.restauranteController.cadastrarRestaurante(novoRestauranteDTO);
    }

}
