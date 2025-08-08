package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.CategoriaProducto;
import com.hmvsoluciones.domain.Producto;
import com.hmvsoluciones.service.dto.CategoriaProductoDTO;
import com.hmvsoluciones.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "categoriaProducto", source = "categoriaProducto", qualifiedByName = "categoriaProductoId")
    ProductoDTO toDto(Producto s);

    @Named("categoriaProductoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoriaProductoDTO toDtoCategoriaProductoId(CategoriaProducto categoriaProducto);
}
