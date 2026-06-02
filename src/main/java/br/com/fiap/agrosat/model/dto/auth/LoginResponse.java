package br.com.fiap.agrosat.model.dto.auth;

import java.time.Instant;

public record LoginResponse(
        String token,
        String tipo,
        Instant expiracaoEm,
        UsuarioResponse usuario
) {}