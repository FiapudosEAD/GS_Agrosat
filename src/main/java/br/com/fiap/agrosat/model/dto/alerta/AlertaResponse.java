package br.com.fiap.agrosat.model.dto.alerta;

import java.time.LocalDateTime;

public record AlertaResponse(

        Long id,

        Long talhaoId,

        String tipo,

        String severidade,

        String mensagem,

        Integer lido,

        LocalDateTime geradoEm
) {}