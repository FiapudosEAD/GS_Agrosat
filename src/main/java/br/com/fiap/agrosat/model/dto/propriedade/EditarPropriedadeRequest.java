package br.com.fiap.agrosat.model.dto.propriedade;

import jakarta.validation.constraints.*;

public record EditarPropriedadeRequest(

        @Size(max = 100)
        String nome,

        @DecimalMin("0.01")
        Double areaHectares,

        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double latitude,

        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double longitude
) {}