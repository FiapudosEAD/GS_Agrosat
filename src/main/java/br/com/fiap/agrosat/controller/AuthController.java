package br.com.fiap.agrosat.controller;

import br.com.fiap.agrosat.model.dto.auth.*;

import br.com.fiap.agrosat.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(
            @Valid
            @RequestBody RegisterRequest request) {

        authService.register(request);

        return "Produtor cadastrado com sucesso!";
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}