package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.CompraTestSamples.*;
import static com.hmvsoluciones.domain.ProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proveedor.class);
        Proveedor proveedor1 = getProveedorSample1();
        Proveedor proveedor2 = new Proveedor();
        assertThat(proveedor1).isNotEqualTo(proveedor2);

        proveedor2.setId(proveedor1.getId());
        assertThat(proveedor1).isEqualTo(proveedor2);

        proveedor2 = getProveedorSample2();
        assertThat(proveedor1).isNotEqualTo(proveedor2);
    }

    @Test
    void compraTest() {
        Proveedor proveedor = getProveedorRandomSampleGenerator();
        Compra compraBack = getCompraRandomSampleGenerator();

        proveedor.addCompra(compraBack);
        assertThat(proveedor.getCompras()).containsOnly(compraBack);
        assertThat(compraBack.getProveedor()).isEqualTo(proveedor);

        proveedor.removeCompra(compraBack);
        assertThat(proveedor.getCompras()).doesNotContain(compraBack);
        assertThat(compraBack.getProveedor()).isNull();

        proveedor.compras(new HashSet<>(Set.of(compraBack)));
        assertThat(proveedor.getCompras()).containsOnly(compraBack);
        assertThat(compraBack.getProveedor()).isEqualTo(proveedor);

        proveedor.setCompras(new HashSet<>());
        assertThat(proveedor.getCompras()).doesNotContain(compraBack);
        assertThat(compraBack.getProveedor()).isNull();
    }
}
