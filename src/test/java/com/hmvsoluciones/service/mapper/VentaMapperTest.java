package com.hmvsoluciones.service.mapper;

import static com.hmvsoluciones.domain.VentaAsserts.*;
import static com.hmvsoluciones.domain.VentaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentaMapperTest {

    private VentaMapper ventaMapper;

    @BeforeEach
    void setUp() {
        ventaMapper = new VentaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVentaSample1();
        var actual = ventaMapper.toEntity(ventaMapper.toDto(expected));
        assertVentaAllPropertiesEquals(expected, actual);
    }
}
