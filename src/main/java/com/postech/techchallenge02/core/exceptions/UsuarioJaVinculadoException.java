package com.postech.techchallenge02.core.exceptions;

public class UsuarioJaVinculadoException extends RuntimeException {

    public UsuarioJaVinculadoException(String message) {
        super(message);
    }
}
