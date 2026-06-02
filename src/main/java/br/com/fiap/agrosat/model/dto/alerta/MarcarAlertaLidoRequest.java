package br.com.fiap.agrosat.model.dto.alerta;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MarcarAlertaLidoRequest(

        @NotNull
        @Min(0)
        @Max(1)
        Integer lido

) {}