package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.exception.ResourceNotFoundException;
import br.com.fiap.agrosat.model.dto.propriedade.*;
import br.com.fiap.agrosat.model.entity.*;
import br.com.fiap.agrosat.model.dto.talhao.TalhaoResponse;

import br.com.fiap.agrosat.repository.*;
import br.com.fiap.agrosat.repository.TalhaoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;

    private final UsuarioRepository usuarioRepository;

    private final TalhaoRepository talhaoRepository;

    public void criar(
            Long usuarioId,
            CriarPropriedadeRequest request
    ){

        if(propriedadeRepository.existsByNomeAndUsuarioId(
                request.nome(),
                usuarioId
        )){

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Propriedade já cadastrada"
            );
        }

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow();

        Propriedade propriedade = Propriedade.builder()
                .nome(request.nome())
                .areaHectares(request.areaHectares())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .usuario(usuario)
                .build();

        propriedadeRepository.save(propriedade);
    }

    public List<PropriedadeResponse> listar(Long usuarioId){

        return propriedadeRepository
                .findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PropriedadeResponse buscar(
            Long id,
            Long usuarioId
    ){

        return propriedadeRepository
                .findByIdAndUsuarioId(id, usuarioId)
                .map(this::toResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registro não encontrado"
                        ));
    }

    public void atualizar(
            Long id,
            Long usuarioId,
            EditarPropriedadeRequest request
    ){

        Propriedade propriedade =
                propriedadeRepository
                        .findByIdAndUsuarioId(id, usuarioId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registro não encontrado"
                                ));

        if(request.nome() != null)
            propriedade.setNome(request.nome());

        if(request.areaHectares() != null)
            propriedade.setAreaHectares(
                    request.areaHectares()
            );

        if(request.latitude() != null)
            propriedade.setLatitude(
                    request.latitude()
            );

        if(request.longitude() != null)
            propriedade.setLongitude(
                    request.longitude()
            );

        propriedadeRepository.save(propriedade);
    }

    public void deletar(
            Long id,
            Long usuarioId
    ){

        Propriedade propriedade =
                propriedadeRepository
                        .findByIdAndUsuarioId(id, usuarioId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registro não encontrado"
                                ));

        propriedadeRepository.delete(propriedade);
    }

    public List<TalhaoResponse> listarTalhoes(
            Long propriedadeId,
            Long usuarioId
    ) {

        propriedadeRepository
                .findByIdAndUsuarioId(
                        propriedadeId,
                        usuarioId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registro não encontrado"
                        ));

        return talhaoRepository
                .findByPropriedadeId(propriedadeId)
                .stream()
                .map(this::toTalhaoResponse)
                .toList();
    }

    private PropriedadeResponse toResponse(
            Propriedade propriedade
    ){

        return new PropriedadeResponse(
                propriedade.getId(),
                propriedade.getNome(),
                propriedade.getAreaHectares(),
                propriedade.getLatitude(),
                propriedade.getLongitude()
        );
    }

    private TalhaoResponse toTalhaoResponse(
            Talhao talhao
    ) {

        return new TalhaoResponse(
                talhao.getId(),
                talhao.getNome(),
                talhao.getCultura(),
                talhao.getDataPlantio(),
                talhao.getAreaHectares(),
                talhao.getPropriedade().getId()
        );
    }
}