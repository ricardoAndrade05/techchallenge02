package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;

public record RestauranteDTO(
    Long id, 
    String nome,
    TipoCozinhaEnun tipoCozinha, 
    String horarioFuncionamento, 
    EnderecoDTO endereco,
    UsuarioDonoDTO dono) {

}
