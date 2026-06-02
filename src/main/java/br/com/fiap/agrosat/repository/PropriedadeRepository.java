package br.com.fiap.agrosat.repository;

import br.com.fiap.agrosat.model.entity.Propriedade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropriedadeRepository
        extends JpaRepository<Propriedade, Long> {

    List<Propriedade> findByUsuarioId(Long usuarioId);

    Optional<Propriedade> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );

    boolean existsByNomeAndUsuarioId(
            String nome,
            Long usuarioId
    );
}