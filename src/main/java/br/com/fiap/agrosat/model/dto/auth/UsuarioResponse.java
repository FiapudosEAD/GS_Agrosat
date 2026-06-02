package br.com.fiap.agrosat.model.dto.auth;

public record UsuarioResponse(
        Long id,
        String nome,
        String email
) {}