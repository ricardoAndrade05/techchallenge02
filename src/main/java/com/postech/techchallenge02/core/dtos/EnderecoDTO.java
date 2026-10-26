package com.postech.techchallenge02.core.dtos;

public record EnderecoDTO(
    Long id, 
    String logradouro,
    String numero,
    String cidade,
    String estado,
    String cep) {

}
