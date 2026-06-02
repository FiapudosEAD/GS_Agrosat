package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "dado_satelite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadoSatelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double ndvi;

    @Column(name = "temp_superficie")
    private Double tempSuperficie;

    @Column(name = "umidade_solo_estimada")
    private Double umidadeSoloEstimada;

    @Column(name = "data_captura")
    private LocalDate dataCaptura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talhao_id")
    private Talhao talhao;
}