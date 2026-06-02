package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import java.util.List;

@Entity
@Table(name = "talhao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Talhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cultura;

    @Column(name = "data_plantio")
    private LocalDate dataPlantio;

    @Column(name = "area_hectares")
    private Double areaHectares;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;

    @OneToMany(mappedBy = "talhao")
    private List<Sensor> sensores;

    @OneToMany(mappedBy = "talhao")
    private List<Alerta> alertas;

    @OneToMany(mappedBy = "talhao")
    private List<DadoSatelite> dadosSatelite;
}