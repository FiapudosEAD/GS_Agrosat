package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.exception.ResourceNotFoundException;

import br.com.fiap.agrosat.model.dto.satelite.DadoSateliteResponse;

import br.com.fiap.agrosat.repository.DadoSateliteRepository;
import br.com.fiap.agrosat.repository.TalhaoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DadoSateliteService {

    private final DadoSateliteRepository repository;

    private final TalhaoRepository talhaoRepository;

    public List<DadoSateliteResponse> listar(
            Long talhaoId,
            Long usuarioId
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

        return repository
                .findByTalhaoIdOrderByDataCapturaDesc(
                        talhaoId
                )
                .stream()
                .map(dado ->
                        new DadoSateliteResponse(
                                dado.getId(),
                                dado.getTalhao().getId(),
                                dado.getNdvi(),
                                dado.getTempSuperficie(),
                                dado.getUmidadeSoloEstimada(),
                                dado.getDataCaptura()
                        ))
                .toList();
    }
}