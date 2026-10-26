package com.postech.techchallenge02.core.dtos;

import java.time.LocalTime;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;

public record RestauranteDTO(
    Long id, 
    String nome,
    TipoCozinhaEnun tipoCozinha, 
    LocalTime horarioInicio,
    LocalTime horarioFim,
    EnderecoDTO endereco,
    UsuarioDonoDTO dono) {

}
