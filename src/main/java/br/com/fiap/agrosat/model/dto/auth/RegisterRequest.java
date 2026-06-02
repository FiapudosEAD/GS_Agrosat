package br.com.fiap.agrosat.model.dto.auth;

import br.com.fiap.agrosat.model.entity.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank
        @Size(min = 3, max = 100)
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 50)
        String senha,

        Role role
) {}