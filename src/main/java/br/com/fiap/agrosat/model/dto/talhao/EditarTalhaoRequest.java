package br.com.fiap.agrosat.model.dto.talhao;

import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDate;

public record EditarTalhaoRequest(

        String nome,

        String cultura,

        LocalDate dataPlantio,

        @DecimalMin("0.01")
        Double areaHectares,

        Long propriedadeId
) {}