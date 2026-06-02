package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sensor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private String modelo;

    private Integer ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talhao_id")
    private Talhao talhao;

    @OneToMany(mappedBy = "sensor")
    private List<Leitura> leituras;
}