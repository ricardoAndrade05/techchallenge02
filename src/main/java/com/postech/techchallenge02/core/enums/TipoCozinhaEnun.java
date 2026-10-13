package com.postech.techchallenge02.core.enums;

import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

public enum TipoCozinhaEnun {

    BRASILEIRA(1, "Brasileira"),
    ITALIANA(2, "Italiana"),
    JAPONESA(3, "Japonesa"),
    AMERICANA(4, "Americana"),
    CHINESA(5, "Chinesa"),
    MEXICANA(6, "Mexicana"),
    ARABE(7, "Árabe"),
    INDIANA(8, "Indiana");

    private final int codigo;
    private final String descricao;

    TipoCozinhaEnun(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCozinhaEnun fromCodigo(int codigo) {
        for (TipoCozinhaEnun tipo : TipoCozinhaEnun.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new RecursoNaoEncontradoException("Código de tipo de cozinha inválido: " + codigo);
    }
}