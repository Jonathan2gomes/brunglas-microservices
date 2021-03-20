package org.acme.cadastro.rest.mapper;

import org.acme.cadastro.domain.Restaurante;
import org.acme.cadastro.rest.dto.RestauranteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nome", source = "nomeFantasia")
    public Restaurante toRestaurante(RestauranteDTO dto);
}
