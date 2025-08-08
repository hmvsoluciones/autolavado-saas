package com.hmvsoluciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoriaProductoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoriaProducto getCategoriaProductoSample1() {
        return new CategoriaProducto().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static CategoriaProducto getCategoriaProductoSample2() {
        return new CategoriaProducto().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static CategoriaProducto getCategoriaProductoRandomSampleGenerator() {
        return new CategoriaProducto()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
