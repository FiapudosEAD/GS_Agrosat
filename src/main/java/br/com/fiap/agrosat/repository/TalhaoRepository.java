package br.com.fiap.agrosat.repository;

import br.com.fiap.agrosat.model.entity.Talhao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TalhaoRepository
        extends JpaRepository<Talhao, Long> {

    List<Talhao> findByPropriedadeId(Long propriedadeId);

    Optional<Talhao> findByIdAndPropriedadeId(
            Long id,
            Long propriedadeId
    );

    @Query("""
            SELECT t
            FROM Talhao t 
            WHERE t.id = :talhaoId 
                AND t.propriedade.usuario.id = :usuarioId
    """)
    Optional<Talhao> findByIdAndUsuarioId(
            @Param("talhaoId") Long talhaoId,
            @Param("usuarioId") Long usuarioId
    );
}