package com.postech.techchallenge02.infra.dtos;

import java.net.URI;

public record ErroPadraoDTO(
    String type,
    String title,
    Integer status,
    String detail,
    URI instance
) {
}
