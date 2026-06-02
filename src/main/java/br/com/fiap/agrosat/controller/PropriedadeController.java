package br.com.fiap.agrosat.controller;

import br.com.fiap.agrosat.model.dto.propriedade.*;
import br.com.fiap.agrosat.model.dto.talhao.TalhaoResponse;
import br.com.fiap.agrosat.security.JwtUtil;
import br.com.fiap.agrosat.service.PropriedadeService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeService service;

    private final JwtUtil jwtUtil;

    private Long getUsuarioId(String authHeader){

        String token =
                authHeader.replace("Bearer ", "");

        return jwtUtil.getUserId(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String criar(
            @RequestHeader("Authorization")
            String authHeader,

            @Valid
            @RequestBody
            CriarPropriedadeRequest request
    ){

        service.criar(
                getUsuarioId(authHeader),
                request
        );

        return "Propriedade cadastrada com sucesso!";
    }

    @GetMapping
    public List<PropriedadeResponse> listar(
            @RequestHeader("Authorization")
            String authHeader
    ){

        return service.listar(
                getUsuarioId(authHeader)
        );
    }

    @GetMapping("/{id}")
    public PropriedadeResponse buscar(
            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ){

        return service.buscar(
                id,
                getUsuarioId(authHeader)
        );
    }

    @PutMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader,

            @RequestBody
            EditarPropriedadeRequest request
    ){

        service.atualizar(
                id,
                getUsuarioId(authHeader),
                request
        );

        return "Propriedade atualizada com sucesso!";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ){

        service.deletar(
                id,
                getUsuarioId(authHeader)
        );
    }

    @GetMapping("/{id}/talhoes")
    public ResponseEntity<List<TalhaoResponse>> listarTalhoes(
            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        List<TalhaoResponse> talhoes =
                service.listarTalhoes(
                        id,
                        getUsuarioId(authHeader)
                );

        if (talhoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(talhoes);
    }
}