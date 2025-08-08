package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.DetalleVenta;
import com.hmvsoluciones.domain.Producto;
import com.hmvsoluciones.domain.Venta;
import com.hmvsoluciones.service.dto.DetalleVentaDTO;
import com.hmvsoluciones.service.dto.ProductoDTO;
import com.hmvsoluciones.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DetalleVenta} and its DTO {@link DetalleVentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DetalleVentaMapper extends EntityMapper<DetalleVentaDTO, DetalleVenta> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoId")
    DetalleVentaDTO toDto(DetalleVenta s);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);

    @Named("productoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductoDTO toDtoProductoId(Producto producto);
}
