package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.exception.ResourceNotFoundException;

import br.com.fiap.agrosat.model.dto.leitura.LeituraResponse;

import br.com.fiap.agrosat.model.entity.Leitura;

import br.com.fiap.agrosat.repository.LeituraRepository;
import br.com.fiap.agrosat.repository.TalhaoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeituraService {

    private final LeituraRepository leituraRepository;

    private final TalhaoRepository talhaoRepository;

    public List<LeituraResponse> listarLeituras(
            Long talhaoId,
            Long usuarioId,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {

        talhaoRepository
                .findByIdAndUsuarioId(
                        talhaoId,
                        usuarioId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registro não encontrado"
                        ));

        List<Leitura> leituras;

        if (dataInicial != null && dataFinal != null) {

            leituras =
                    leituraRepository
                            .buscarPorTalhaoEPeriodo(
                                    talhaoId,
                                    dataInicial.atStartOfDay(),
                                    dataFinal.atTime(
                                            23,
                                            59,
                                            59
                                    )
                            );

        } else {

            leituras =
                    leituraRepository
                            .buscarPorTalhao(
                                    talhaoId
                            );
        }

        return leituras
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LeituraResponse toResponse(
            Leitura leitura
    ) {

        return new LeituraResponse(
                leitura.getId(),
                leitura.getSensor().getId(),
                leitura.getValor(),
                leitura.getUnidade(),
                leitura.getRegistradoEm()
        );
    }
}