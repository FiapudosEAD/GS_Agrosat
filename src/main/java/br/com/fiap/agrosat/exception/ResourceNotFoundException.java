package br.com.fiap.agrosat.exception;

public class ResourceNotFoundException
        extends RuntimeException {

    public ResourceNotFoundException(
            String message
    ){
        super(message);
    }
}