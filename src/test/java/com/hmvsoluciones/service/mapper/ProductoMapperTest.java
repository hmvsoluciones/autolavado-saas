package com.hmvsoluciones.service.mapper;

import static com.hmvsoluciones.domain.ProductoAsserts.*;
import static com.hmvsoluciones.domain.ProductoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoMapperTest {

    private ProductoMapper productoMapper;

    @BeforeEach
    void setUp() {
        productoMapper = new ProductoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductoSample1();
        var actual = productoMapper.toEntity(productoMapper.toDto(expected));
        assertProductoAllPropertiesEquals(expected, actual);
    }
}
