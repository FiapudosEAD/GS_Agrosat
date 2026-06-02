package br.com.fiap.agrosat.model.dto.propriedade;

public record PropriedadeResponse(

        Long id,
        String nome,
        Double areaHectares,
        Double latitude,
        Double longitude
) {}