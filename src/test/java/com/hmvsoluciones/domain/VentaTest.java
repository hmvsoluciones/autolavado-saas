package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.ClienteTestSamples.*;
import static com.hmvsoluciones.domain.DetalleVentaTestSamples.*;
import static com.hmvsoluciones.domain.FacturaTestSamples.*;
import static com.hmvsoluciones.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = getVentaSample1();
        Venta venta2 = new Venta();
        assertThat(venta1).isNotEqualTo(venta2);

        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);

        venta2 = getVentaSample2();
        assertThat(venta1).isNotEqualTo(venta2);
    }

    @Test
    void clienteTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        venta.setCliente(clienteBack);
        assertThat(venta.getCliente()).isEqualTo(clienteBack);

        venta.cliente(null);
        assertThat(venta.getCliente()).isNull();
    }

    @Test
    void detalleVentaTest() {
        Venta venta = getVentaRandomSampleGenerator();
        DetalleVenta detalleVentaBack = getDetalleVentaRandomSampleGenerator();

        venta.addDetalleVenta(detalleVentaBack);
        assertThat(venta.getDetalleVentas()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isEqualTo(venta);

        venta.removeDetalleVenta(detalleVentaBack);
        assertThat(venta.getDetalleVentas()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isNull();

        venta.detalleVentas(new HashSet<>(Set.of(detalleVentaBack)));
        assertThat(venta.getDetalleVentas()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isEqualTo(venta);

        venta.setDetalleVentas(new HashSet<>());
        assertThat(venta.getDetalleVentas()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isNull();
    }

    @Test
    void facturaTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Factura facturaBack = getFacturaRandomSampleGenerator();

        venta.addFactura(facturaBack);
        assertThat(venta.getFacturas()).containsOnly(facturaBack);
        assertThat(facturaBack.getVenta()).isEqualTo(venta);

        venta.removeFactura(facturaBack);
        assertThat(venta.getFacturas()).doesNotContain(facturaBack);
        assertThat(facturaBack.getVenta()).isNull();

        venta.facturas(new HashSet<>(Set.of(facturaBack)));
        assertThat(venta.getFacturas()).containsOnly(facturaBack);
        assertThat(facturaBack.getVenta()).isEqualTo(venta);

        venta.setFacturas(new HashSet<>());
        assertThat(venta.getFacturas()).doesNotContain(facturaBack);
        assertThat(facturaBack.getVenta()).isNull();
    }
}
