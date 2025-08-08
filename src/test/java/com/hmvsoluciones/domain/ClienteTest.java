package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.ClienteTestSamples.*;
import static com.hmvsoluciones.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void ventaTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        cliente.addVenta(ventaBack);
        assertThat(cliente.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getCliente()).isEqualTo(cliente);

        cliente.removeVenta(ventaBack);
        assertThat(cliente.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getCliente()).isNull();

        cliente.ventas(new HashSet<>(Set.of(ventaBack)));
        assertThat(cliente.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getCliente()).isEqualTo(cliente);

        cliente.setVentas(new HashSet<>());
        assertThat(cliente.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getCliente()).isNull();
    }
}
