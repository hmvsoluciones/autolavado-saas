package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.Proveedor;
import com.hmvsoluciones.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proveedor} and its DTO {@link ProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProveedorMapper extends EntityMapper<ProveedorDTO, Proveedor> {}
