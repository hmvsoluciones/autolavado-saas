package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.CompraTestSamples.*;
import static com.hmvsoluciones.domain.ProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compra.class);
        Compra compra1 = getCompraSample1();
        Compra compra2 = new Compra();
        assertThat(compra1).isNotEqualTo(compra2);

        compra2.setId(compra1.getId());
        assertThat(compra1).isEqualTo(compra2);

        compra2 = getCompraSample2();
        assertThat(compra1).isNotEqualTo(compra2);
    }

    @Test
    void proveedorTest() {
        Compra compra = getCompraRandomSampleGenerator();
        Proveedor proveedorBack = getProveedorRandomSampleGenerator();

        compra.setProveedor(proveedorBack);
        assertThat(compra.getProveedor()).isEqualTo(proveedorBack);

        compra.proveedor(null);
        assertThat(compra.getProveedor()).isNull();
    }
}
