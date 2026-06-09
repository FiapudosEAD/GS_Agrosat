package br.com.fiap.agrosat.repository;

import br.com.fiap.agrosat.model.entity.DadoSatelite;
import br.com.fiap.agrosat.model.entity.Talhao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DadoSateliteRepository
        extends JpaRepository<DadoSatelite, Long> {

    List<DadoSatelite>
    findByTalhaoIdOrderByDataCapturaDesc(
            Long talhaoId
    );

    List<DadoSatelite> findByTalhaoAndDataCapturaAfterOrderByDataCapturaAsc(Talhao talhao, LocalDate dataCaptura);
}