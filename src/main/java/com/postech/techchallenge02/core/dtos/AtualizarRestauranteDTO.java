package com.postech.techchallenge02.core.dtos;

import java.time.LocalTime;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;

public record AtualizarRestauranteDTO(
        Long idRestaurante,
        String nome,
        EnderecoDTO endereco,
        TipoCozinhaEnun tipoCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,

        Long idDono,

        Long idUsuarioLogado

) {

}
