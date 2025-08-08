package com.hmvsoluciones.service.mapper;

import static com.hmvsoluciones.domain.FacturaAsserts.*;
import static com.hmvsoluciones.domain.FacturaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacturaMapperTest {

    private FacturaMapper facturaMapper;

    @BeforeEach
    void setUp() {
        facturaMapper = new FacturaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFacturaSample1();
        var actual = facturaMapper.toEntity(facturaMapper.toDto(expected));
        assertFacturaAllPropertiesEquals(expected, actual);
    }
}
