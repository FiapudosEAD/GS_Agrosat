package br.com.fiap.agrosat.controller;

import br.com.fiap.agrosat.model.dto.alerta.MarcarAlertaLidoRequest;

import br.com.fiap.agrosat.security.JwtUtil;

import br.com.fiap.agrosat.service.AlertaService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    private final JwtUtil jwtUtil;

    private Long getUsuarioId(
            String authHeader
    ) {

        String token =
                authHeader.replace(
                        "Bearer ",
                        ""
                );

        return jwtUtil.getUserId(token);
    }

    @PutMapping("/{id}/marcar-lido")
    public String marcarComoLido(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader,

            @Valid
            @RequestBody
            MarcarAlertaLidoRequest request
    ) {

        alertaService.marcarComoLido(
                id,
                getUsuarioId(authHeader),
                request.lido()
        );

        return "Alerta marcado como lido!";
    }
}