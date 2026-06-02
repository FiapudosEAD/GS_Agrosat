package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_agrosat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private Role role;
}