package com.hmvsoluciones.domain;

import static com.hmvsoluciones.domain.CategoriaProductoTestSamples.*;
import static com.hmvsoluciones.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoriaProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaProducto.class);
        CategoriaProducto categoriaProducto1 = getCategoriaProductoSample1();
        CategoriaProducto categoriaProducto2 = new CategoriaProducto();
        assertThat(categoriaProducto1).isNotEqualTo(categoriaProducto2);

        categoriaProducto2.setId(categoriaProducto1.getId());
        assertThat(categoriaProducto1).isEqualTo(categoriaProducto2);

        categoriaProducto2 = getCategoriaProductoSample2();
        assertThat(categoriaProducto1).isNotEqualTo(categoriaProducto2);
    }

    @Test
    void productoTest() {
        CategoriaProducto categoriaProducto = getCategoriaProductoRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        categoriaProducto.addProducto(productoBack);
        assertThat(categoriaProducto.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getCategoriaProducto()).isEqualTo(categoriaProducto);

        categoriaProducto.removeProducto(productoBack);
        assertThat(categoriaProducto.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getCategoriaProducto()).isNull();

        categoriaProducto.productos(new HashSet<>(Set.of(productoBack)));
        assertThat(categoriaProducto.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getCategoriaProducto()).isEqualTo(categoriaProducto);

        categoriaProducto.setProductos(new HashSet<>());
        assertThat(categoriaProducto.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getCategoriaProducto()).isNull();
    }
}
