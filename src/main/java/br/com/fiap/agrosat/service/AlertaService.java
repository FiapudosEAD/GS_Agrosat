package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.exception.ResourceNotFoundException;

import br.com.fiap.agrosat.model.entity.Alerta;

import br.com.fiap.agrosat.model.dto.alerta.AlertaResponse;

import br.com.fiap.agrosat.repository.AlertaRepository;
import br.com.fiap.agrosat.repository.TalhaoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;

    private final TalhaoRepository talhaoRepository;

    public List<AlertaResponse> listar(
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

        return alertaRepository
                .findByTalhaoIdOrderByGeradoEmDesc(
                        talhaoId
                )
                .stream()
                .map(alerta ->
                        new AlertaResponse(
                                alerta.getId(),
                                alerta.getTalhao().getId(),
                                alerta.getTipo(),
                                alerta.getSeveridade(),
                                alerta.getMensagem(),
                                alerta.getLido(),
                                alerta.getGeradoEm()
                        ))
                .toList();
    }

    public void marcarComoLido(
            Long alertaId,
            Long usuarioId,
            Integer lido
    ) {

        Alerta alerta =
                alertaRepository
                        .findByIdAndUsuarioId(
                                alertaId,
                                usuarioId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registro não encontrado"
                                ));

        alerta.setLido(lido);

        alertaRepository.save(alerta);
    }
}