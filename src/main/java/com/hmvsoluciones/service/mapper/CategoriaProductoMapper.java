package com.hmvsoluciones.service.mapper;

import com.hmvsoluciones.domain.CategoriaProducto;
import com.hmvsoluciones.service.dto.CategoriaProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaProducto} and its DTO {@link CategoriaProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaProductoMapper extends EntityMapper<CategoriaProductoDTO, CategoriaProducto> {}
