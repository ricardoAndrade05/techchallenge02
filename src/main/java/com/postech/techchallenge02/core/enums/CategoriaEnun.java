package com.postech.techchallenge02.core.enums;

import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

public enum CategoriaEnun {

    DONO(1, "Dono de Restaurante"),
    CLIENTE(2, "Cliente");

    private Integer codigo;
    private String descricao;

    private CategoriaEnun(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static CategoriaEnun fromCodigo(Integer codigo) {
        if (codigo == null) {
            return null;
        }

        for (CategoriaEnun tipo : CategoriaEnun.values()) {
            if (tipo.getCodigo().equals(codigo)) {
                return tipo;
            }
        }
        throw new RecursoNaoEncontradoException("Código de tipo de usuário inválido: " + codigo);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

}
