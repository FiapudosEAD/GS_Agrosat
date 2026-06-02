package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leitura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valor;

    private String unidade;

    @Column(name = "registrado_em")
    private LocalDateTime registradoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
}