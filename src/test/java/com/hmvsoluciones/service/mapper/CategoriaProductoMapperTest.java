package com.hmvsoluciones.service.mapper;

import static com.hmvsoluciones.domain.CategoriaProductoAsserts.*;
import static com.hmvsoluciones.domain.CategoriaProductoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaProductoMapperTest {

    private CategoriaProductoMapper categoriaProductoMapper;

    @BeforeEach
    void setUp() {
        categoriaProductoMapper = new CategoriaProductoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoriaProductoSample1();
        var actual = categoriaProductoMapper.toEntity(categoriaProductoMapper.toDto(expected));
        assertCategoriaProductoAllPropertiesEquals(expected, actual);
    }
}
