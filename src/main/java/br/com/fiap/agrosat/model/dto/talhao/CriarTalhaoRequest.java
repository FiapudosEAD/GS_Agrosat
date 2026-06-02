package br.com.fiap.agrosat.model.dto.talhao;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CriarTalhaoRequest(

        @NotBlank
        @Size(max = 50)
        String nome,

        @NotBlank
        @Size(max = 50)
        String cultura,

        LocalDate dataPlantio,

        @DecimalMin("0.01")
        Double areaHectares,

        @NotNull
        Long propriedadeId
) {}