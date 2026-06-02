package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.model.dto.auth.*;
import br.com.fiap.agrosat.model.entity.*;

import br.com.fiap.agrosat.repository.UsuarioRepository;

import br.com.fiap.agrosat.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Produtor já cadastrado"
            );
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senhaHash(
                        passwordEncoder.encode(
                                request.senha()
                        )
                )
                .role(Role.PRODUTOR)
                .build();

        usuarioRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario =
                usuarioRepository.findByEmail(request.email())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "E-mail ou senha inválidos"
                                ));

        boolean senhaValida =
                passwordEncoder.matches(
                        request.senha(),
                        usuario.getSenhaHash()
                );

        if (!senhaValida) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos"
            );
        }

        String token =
                jwtUtil.generateToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                jwtUtil.getExpiration(token),
                new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail()
                )
        );
    }
}