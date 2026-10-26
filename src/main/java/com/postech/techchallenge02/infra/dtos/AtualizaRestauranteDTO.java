package com.postech.techchallenge02.infra.dtos;

import java.time.LocalTime;

public record AtualizaRestauranteDTO(
        String nome,
        String logradouro,
        String numero,
        String cidade,
        String estado,
        String cep,
        Integer tipoCozinha,
        LocalTime inicioFuncionamento,
        LocalTime fimFuncionamento,
        Long idDonoRestaurante,
        Long idUsuarioLogado) {

}
