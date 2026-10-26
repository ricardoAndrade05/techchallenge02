package com.postech.techchallenge02.infra.infra.handlers;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioJaVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.infra.dtos.ErroPadraoDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroPadraoDTO> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "recurso-nao-encontrado",
                "Recurso não encontrado",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                URI.create(request.getRequestURI()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler({
            TipoUsuarioExistenteException.class,
            TipoUsuarioVinculadoException.class
    })
    public ResponseEntity<ErroPadraoDTO> handleConflito(RuntimeException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "conflito-de-dados",
                "Conflito de dados",
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                URI.create(request.getRequestURI()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<ErroPadraoDTO> handleDadosInvalidosException(DadosInvalidosException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "requisicao-invalida",
                "Requisição inválida",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                URI.create(request.getRequestURI()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(UsuarioSemPermissaoException.class)
    public ResponseEntity<ErroPadraoDTO> handleUsuarioSemPermissaoException(UsuarioSemPermissaoException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "permissao-insuficiente",
                "Permissão Insuficiente",
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                URI.create(request.getRequestURI()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(UsuarioJaVinculadoException.class)
    public ResponseEntity<ErroPadraoDTO> handleUsuarioJaVinculadoException(UsuarioJaVinculadoException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "conflito-recurso",
                "Conflito de Recurso",
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                URI.create(request.getRequestURI()),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}