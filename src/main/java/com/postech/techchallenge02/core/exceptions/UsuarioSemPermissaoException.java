package com.postech.techchallenge02.core.exceptions;

public class UsuarioSemPermissaoException extends RuntimeException {

    public UsuarioSemPermissaoException(String message) {
        super(message);
    }

}
