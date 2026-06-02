package br.com.fiap.agrosat.model.dto.leitura;

import java.time.LocalDateTime;

public record LeituraResponse(

        Long id,

        Long sensorId,

        Double valor,

        String unidade,

        LocalDateTime registradoEm
) {}