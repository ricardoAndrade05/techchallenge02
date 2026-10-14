package com.postech.techchallenge02.infra.dtos;

import java.net.URI;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErroPadraoDTO(
    String type,
    String title,
    Integer status,
    String detail,
    URI instance,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime timestamp
) {
}
