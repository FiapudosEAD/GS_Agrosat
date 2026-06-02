package br.com.fiap.agrosat.model.dto.propriedade;

import jakarta.validation.constraints.*;

public record CriarPropriedadeRequest(

        @NotBlank
        @Size(max = 100)
        String nome,

        @NotNull
        @DecimalMin("0.01")
        Double areaHectares,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double longitude
) {}