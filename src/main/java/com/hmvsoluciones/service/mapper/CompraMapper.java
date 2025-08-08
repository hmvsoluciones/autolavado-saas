package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.Compra;
import com.hmvsoluciones.domain.Proveedor;
import com.hmvsoluciones.service.dto.CompraDTO;
import com.hmvsoluciones.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "proveedorId")
    CompraDTO toDto(Compra s);

    @Named("proveedorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProveedorDTO toDtoProveedorId(Proveedor proveedor);
}
