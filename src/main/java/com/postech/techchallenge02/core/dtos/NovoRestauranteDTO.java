package com.postech.techchallenge02.core.dtos;

import java.time.LocalTime;

public record NovoRestauranteDTO(
    String nome,
    EnderecoDTO endereco, 
    Integer tipoCozinha,
    LocalTime horarioAbertura,
    LocalTime horarioFechamento,
    Long idUsuarioLogado) {

}
