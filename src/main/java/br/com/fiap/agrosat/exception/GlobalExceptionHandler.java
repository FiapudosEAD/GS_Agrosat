package br.com.fiap.agrosat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<?> validation(
            MethodArgumentNotValidException ex) {

        String msg =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(
                        Map.of(
                                "erro",
                                "VALIDACAO",
                                "mensagem",
                                msg
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generic(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Map.of(
                                "erro",
                                "ERRO_INTERNO",
                                "mensagem",
                                ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> notFound(
            ResourceNotFoundException ex
    ){

        return ResponseEntity.status(404)
                .body(
                        Map.of(
                                "erro",
                                "NAO_ENCONTRADO",
                                "mensagem",
                                ex.getMessage()
                        )
                );
    }
}