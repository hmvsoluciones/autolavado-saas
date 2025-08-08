package com.hmvsoluciones.service.mapper;

import static com.hmvsoluciones.domain.CompraAsserts.*;
import static com.hmvsoluciones.domain.CompraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompraMapperTest {

    private CompraMapper compraMapper;

    @BeforeEach
    void setUp() {
        compraMapper = new CompraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompraSample1();
        var actual = compraMapper.toEntity(compraMapper.toDto(expected));
        assertCompraAllPropertiesEquals(expected, actual);
    }
}
