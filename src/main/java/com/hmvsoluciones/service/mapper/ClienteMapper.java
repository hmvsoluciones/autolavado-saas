package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.Cliente;
import com.hmvsoluciones.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {}
