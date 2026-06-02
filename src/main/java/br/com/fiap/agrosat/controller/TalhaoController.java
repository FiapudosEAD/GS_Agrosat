package br.com.fiap.agrosat.controller;

import br.com.fiap.agrosat.model.dto.talhao.*;
import br.com.fiap.agrosat.model.dto.leitura.LeituraResponse;
import br.com.fiap.agrosat.model.dto.alerta.AlertaResponse;
import br.com.fiap.agrosat.model.dto.satelite.DadoSateliteResponse;

import br.com.fiap.agrosat.security.JwtUtil;

import br.com.fiap.agrosat.service.TalhaoService;
import br.com.fiap.agrosat.service.LeituraService;
import br.com.fiap.agrosat.service.AlertaService;
import br.com.fiap.agrosat.service.DadoSateliteService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/talhoes")
@RequiredArgsConstructor
public class TalhaoController {

    private final TalhaoService service;

    private final LeituraService leituraService;

    private final AlertaService alertaService;

    private final DadoSateliteService dadoSateliteService;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String criar(

            @RequestHeader("Authorization")
            String authHeader,

            @Valid
            @RequestBody
            CriarTalhaoRequest request
    ) {

        service.criar(
                getUsuarioId(authHeader),
                request
        );

        return "Talhão cadastrado com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizar(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader,

            @RequestBody
            EditarTalhaoRequest request
    ) {

        service.atualizar(
                id,
                getUsuarioId(authHeader),
                request
        );

        return "Talhão atualizado com sucesso!";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        service.deletar(
                id,
                getUsuarioId(authHeader)
        );
    }

    @GetMapping("/{id}/leituras")
    public ResponseEntity<List<LeituraResponse>> listarLeituras(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFinal
    ) {

        List<LeituraResponse> leituras =
                leituraService.listarLeituras(
                        id,
                        getUsuarioId(authHeader),
                        dataInicial,
                        dataFinal
                );

        if (leituras.isEmpty()) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(leituras);
    }

    @GetMapping("/{id}/alertas")
    public ResponseEntity<List<AlertaResponse>> listarAlertas(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        List<AlertaResponse> alertas =
                alertaService.listar(
                        id,
                        getUsuarioId(authHeader)
                );

        if (alertas.isEmpty()) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/{id}/dados-satelite")
    public ResponseEntity<List<DadoSateliteResponse>>
    listarDadosSatelite(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        return ResponseEntity.ok(
                dadoSateliteService.listar(
                        id,
                        getUsuarioId(authHeader)
                )
        );
    }
}