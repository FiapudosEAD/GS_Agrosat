package br.com.fiap.agrosat.repository;

import br.com.fiap.agrosat.model.entity.Alerta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlertaRepository
        extends JpaRepository<Alerta, Long> {

    List<Alerta> findByTalhaoIdOrderByGeradoEmDesc(
            Long talhaoId
    );

    @Query("""
        SELECT a
        FROM Alerta a
        WHERE a.id = :alertaId
            AND a.talhao.propriedade.usuario.id = :usuarioId
    """)
    Optional<Alerta> findByIdAndUsuarioId(
            @Param("alertaId") Long alertaId,
            @Param("usuarioId") Long usuarioId
    );
}