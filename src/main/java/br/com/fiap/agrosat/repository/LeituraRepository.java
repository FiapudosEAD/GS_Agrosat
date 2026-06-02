package br.com.fiap.agrosat.repository;

import br.com.fiap.agrosat.model.entity.Leitura;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LeituraRepository
        extends JpaRepository<Leitura, Long> {

    @Query("""
        SELECT l
        FROM Leitura l
        WHERE l.sensor.talhao.id = :talhaoId
        ORDER BY l.registradoEm DESC
    """)
    List<Leitura> buscarPorTalhao(
            @Param("talhaoId") Long talhaoId
    );

    @Query("""
        SELECT l
        FROM Leitura l
        WHERE l.sensor.talhao.id = :talhaoId
          AND l.registradoEm BETWEEN :inicio AND :fim
        ORDER BY l.registradoEm DESC
    """)
    List<Leitura> buscarPorTalhaoEPeriodo(
            @Param("talhaoId") Long talhaoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}