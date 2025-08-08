package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.Factura;
import com.hmvsoluciones.domain.Venta;
import com.hmvsoluciones.service.dto.FacturaDTO;
import com.hmvsoluciones.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    FacturaDTO toDto(Factura s);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
