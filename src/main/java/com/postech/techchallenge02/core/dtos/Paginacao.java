package com.postech.techchallenge02.core.dtos;

public record Paginacao(
    int pagina,
    int tamanho
) {
    public Paginacao {
        if (pagina < 0) {
            pagina = 0;
        }
        if (tamanho <= 0) {
            tamanho = 10;
        }
    }
}
