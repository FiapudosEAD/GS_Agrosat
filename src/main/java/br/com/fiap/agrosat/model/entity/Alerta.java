package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private String severidade;

    private String mensagem;

    private Integer lido;

    @Column(name = "gerado_em")
    private LocalDateTime geradoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talhao_id")
    private Talhao talhao;
}