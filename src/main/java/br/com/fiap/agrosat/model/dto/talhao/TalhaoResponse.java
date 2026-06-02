package br.com.fiap.agrosat.model.dto.talhao;

import java.time.LocalDate;

public record TalhaoResponse(

        Long id,

        String nome,

        String cultura,

        LocalDate dataPlantio,

        Double areaHectares,

        Long propriedadeId
) {}