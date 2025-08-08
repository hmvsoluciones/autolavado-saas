package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.FacturaTestSamples.*;
import static com.hmvsoluciones.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factura.class);
        Factura factura1 = getFacturaSample1();
        Factura factura2 = new Factura();
        assertThat(factura1).isNotEqualTo(factura2);

        factura2.setId(factura1.getId());
        assertThat(factura1).isEqualTo(factura2);

        factura2 = getFacturaSample2();
        assertThat(factura1).isNotEqualTo(factura2);
    }

    @Test
    void ventaTest() {
        Factura factura = getFacturaRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        factura.setVenta(ventaBack);
        assertThat(factura.getVenta()).isEqualTo(ventaBack);

        factura.venta(null);
        assertThat(factura.getVenta()).isNull();
    }
}
