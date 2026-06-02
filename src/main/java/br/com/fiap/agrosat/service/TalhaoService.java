package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.exception.ResourceNotFoundException;

import br.com.fiap.agrosat.model.dto.talhao.*;

import br.com.fiap.agrosat.model.entity.*;

import br.com.fiap.agrosat.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TalhaoService {

    private final TalhaoRepository talhaoRepository;

    private final PropriedadeRepository propriedadeRepository;

    public void criar(
            Long usuarioId,
            CriarTalhaoRequest request
    ) {

        Propriedade propriedade =
                propriedadeRepository
                        .findByIdAndUsuarioId(
                                request.propriedadeId(),
                                usuarioId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registro não encontrado"
                                ));

        boolean existeMesmoNome =
                talhaoRepository
                        .findByPropriedadeId(
                                propriedade.getId()
                        )
                        .stream()
                        .anyMatch(t ->
                                t.getNome()
                                        .equalsIgnoreCase(
                                                request.nome()
                                        )
                        );

        if (existeMesmoNome) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Talhão já cadastrado"
            );
        }

        Talhao talhao = Talhao.builder()
                .nome(request.nome())
                .cultura(request.cultura())
                .dataPlantio(request.dataPlantio())
                .areaHectares(request.areaHectares())
                .propriedade(propriedade)
                .build();

        talhaoRepository.save(talhao);
    }

    public void atualizar(
            Long id,
            Long usuarioId,
            EditarTalhaoRequest request
    ) {

        Talhao talhao =
                buscarTalhaoDoUsuario(
                        id,
                        usuarioId
                );

        if (request.nome() != null)
            talhao.setNome(request.nome());

        if (request.cultura() != null)
            talhao.setCultura(request.cultura());

        if (request.dataPlantio() != null)
            talhao.setDataPlantio(
                    request.dataPlantio()
            );

        if (request.areaHectares() != null)
            talhao.setAreaHectares(
                    request.areaHectares()
            );

        if (request.propriedadeId() != null) {

            Propriedade novaPropriedade =
                    propriedadeRepository
                            .findByIdAndUsuarioId(
                                    request.propriedadeId(),
                                    usuarioId
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Registro não encontrado"
                                    ));

            talhao.setPropriedade(
                    novaPropriedade
            );
        }

        talhaoRepository.save(talhao);
    }

    public void deletar(
            Long id,
            Long usuarioId
    ) {

        Talhao talhao =
                buscarTalhaoDoUsuario(
                        id,
                        usuarioId
                );

        talhaoRepository.delete(talhao);
    }

    private Talhao buscarTalhaoDoUsuario(
            Long talhaoId,
            Long usuarioId
    ) {

        return talhaoRepository.findByIdAndUsuarioId(
                talhaoId,
                usuarioId
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Registro não encontrado"
                ));
    }
}