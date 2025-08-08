package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.CategoriaProductoTestSamples.*;
import static com.hmvsoluciones.domain.DetalleVentaTestSamples.*;
import static com.hmvsoluciones.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void categoriaProductoTest() {
        Producto producto = getProductoRandomSampleGenerator();
        CategoriaProducto categoriaProductoBack = getCategoriaProductoRandomSampleGenerator();

        producto.setCategoriaProducto(categoriaProductoBack);
        assertThat(producto.getCategoriaProducto()).isEqualTo(categoriaProductoBack);

        producto.categoriaProducto(null);
        assertThat(producto.getCategoriaProducto()).isNull();
    }

    @Test
    void detalleVentaTest() {
        Producto producto = getProductoRandomSampleGenerator();
        DetalleVenta detalleVentaBack = getDetalleVentaRandomSampleGenerator();

        producto.addDetalleVenta(detalleVentaBack);
        assertThat(producto.getDetalleVentas()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getProducto()).isEqualTo(producto);

        producto.removeDetalleVenta(detalleVentaBack);
        assertThat(producto.getDetalleVentas()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getProducto()).isNull();

        producto.detalleVentas(new HashSet<>(Set.of(detalleVentaBack)));
        assertThat(producto.getDetalleVentas()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getProducto()).isEqualTo(producto);

        producto.setDetalleVentas(new HashSet<>());
        assertThat(producto.getDetalleVentas()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getProducto()).isNull();
    }
}
