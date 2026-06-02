package br.com.fiap.agrosat.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "propriedade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "area_hectares")
    private Double areaHectares;

    private Double latitude;

    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(
            mappedBy = "propriedade",
            cascade = CascadeType.ALL
    )
    private List<Talhao> talhoes;
}