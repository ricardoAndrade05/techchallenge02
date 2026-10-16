package com.postech.techchallenge02.core.dtos;

public record AtualizarTipoUsuarioDTO(
    Long idTipoUsuario, 
    String novoNome, 
    Long idUsuarioLogado,
    Long idRestaurante) {} 