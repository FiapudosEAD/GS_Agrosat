package br.com.fiap.agrosat.model.dto.satelite;

import java.time.LocalDate;

public record DadoSateliteResponse(

        Long id,

        Long talhaoId,

        Double ndvi,

        Double tempSuperficie,

        Double umidadeSoloEstimada,

        LocalDate dataCaptura
) {}