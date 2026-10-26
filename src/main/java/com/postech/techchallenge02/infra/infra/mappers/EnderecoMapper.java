package com.postech.techchallenge02.infra.infra.mappers;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.infra.entities.Endereco;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoDTO enderecoDTO) {
        if (enderecoDTO == null) {
            return null;
        }

        Endereco endereco = new Endereco();

        endereco.setId(enderecoDTO.id());
        endereco.setLogradouro(enderecoDTO.logradouro());
        endereco.setNumero(enderecoDTO.numero());
        endereco.setCidade(enderecoDTO.cidade());
        endereco.setEstado(enderecoDTO.estado());
        endereco.setCep(enderecoDTO.cep());

        return endereco;
    }

    public EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        Long id = null;
        String logradouro = null;
        String numero = null;
        String cidade = null;
        String estado = null;
        String cep = null;

        id = endereco.getId();
        logradouro = endereco.getLogradouro();
        numero = endereco.getNumero();
        cidade = endereco.getCidade();
        estado = endereco.getEstado();
        cep = endereco.getCep();

        EnderecoDTO enderecoDTO = new EnderecoDTO(id, logradouro, numero, cidade, estado, cep);

        return enderecoDTO;
    }

}
