package br.com.fiap.agrosat.controller;

import br.com.fiap.agrosat.model.dto.auth.*;

import br.com.fiap.agrosat.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticação e registro de usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registrar novo usuário",
        description = "Criar uma nova conta de produtor agrícola"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Usuário registrado com sucesso"
    )
    public String register(
            @Valid
            @RequestBody RegisterRequest request) {

        authService.register(request);

        return "Produtor cadastrado com sucesso!";
    }

    @PostMapping("/login")
    @Operation(
        summary = "Fazer login",
        description = "Autenticar e obter token JWT"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Login bem-sucedido",
        content = @Content(schema = @Schema(implementation = LoginResponse.class))
    )
    public LoginResponse login(
            @Valid
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}