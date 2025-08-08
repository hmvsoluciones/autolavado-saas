package com.hmvsoluciones.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DetalleVentaCriteriaTest {

    @Test
    void newDetalleVentaCriteriaHasAllFiltersNullTest() {
        var detalleVentaCriteria = new DetalleVentaCriteria();
        assertThat(detalleVentaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void detalleVentaCriteriaFluentMethodsCreatesFiltersTest() {
        var detalleVentaCriteria = new DetalleVentaCriteria();

        setAllFilters(detalleVentaCriteria);

        assertThat(detalleVentaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void detalleVentaCriteriaCopyCreatesNullFilterTest() {
        var detalleVentaCriteria = new DetalleVentaCriteria();
        var copy = detalleVentaCriteria.copy();

        assertThat(detalleVentaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(detalleVentaCriteria)
        );
    }

    @Test
    void detalleVentaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var detalleVentaCriteria = new DetalleVentaCriteria();
        setAllFilters(detalleVentaCriteria);

        var copy = detalleVentaCriteria.copy();

        assertThat(detalleVentaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(detalleVentaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var detalleVentaCriteria = new DetalleVentaCriteria();

        assertThat(detalleVentaCriteria).hasToString("DetalleVentaCriteria{}");
    }

    private static void setAllFilters(DetalleVentaCriteria detalleVentaCriteria) {
        detalleVentaCriteria.id();
        detalleVentaCriteria.cantidad();
        detalleVentaCriteria.precioUnitario();
        detalleVentaCriteria.subtotal();
        detalleVentaCriteria.ventaId();
        detalleVentaCriteria.productoId();
        detalleVentaCriteria.distinct();
    }

    private static Condition<DetalleVentaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCantidad()) &&
                condition.apply(criteria.getPrecioUnitario()) &&
                condition.apply(criteria.getSubtotal()) &&
                condition.apply(criteria.getVentaId()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DetalleVentaCriteria> copyFiltersAre(
        DetalleVentaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCantidad(), copy.getCantidad()) &&
                condition.apply(criteria.getPrecioUnitario(), copy.getPrecioUnitario()) &&
                condition.apply(criteria.getSubtotal(), copy.getSubtotal()) &&
                condition.apply(criteria.getVentaId(), copy.getVentaId()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
