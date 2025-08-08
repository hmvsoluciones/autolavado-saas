package com.hmvsoluciones.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FacturaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Factura getFacturaSample1() {
        return new Factura().id(1L).numero("numero1");
    }

    public static Factura getFacturaSample2() {
        return new Factura().id(2L).numero("numero2");
    }

    public static Factura getFacturaRandomSampleGenerator() {
        return new Factura().id(longCount.incrementAndGet()).numero(UUID.randomUUID().toString());
    }
}
