package com.postech.techchallenge02.core.dtos;

import java.util.Collections;
import java.util.List;

public record Pagina<T>(
    List<T> itens,
    int paginaAtual,
    int tamanhoPagina,
    long totalItens,
    int totalPaginas
) {
    public Pagina {
        if (itens == null) {
            itens = Collections.emptyList();
        }
    }

    public boolean estaVazia() {
        return itens.isEmpty();
    }
}
