package com.hmvsoluciones.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CompraCriteriaTest {

    @Test
    void newCompraCriteriaHasAllFiltersNullTest() {
        var compraCriteria = new CompraCriteria();
        assertThat(compraCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void compraCriteriaFluentMethodsCreatesFiltersTest() {
        var compraCriteria = new CompraCriteria();

        setAllFilters(compraCriteria);

        assertThat(compraCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void compraCriteriaCopyCreatesNullFilterTest() {
        var compraCriteria = new CompraCriteria();
        var copy = compraCriteria.copy();

        assertThat(compraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(compraCriteria)
        );
    }

    @Test
    void compraCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var compraCriteria = new CompraCriteria();
        setAllFilters(compraCriteria);

        var copy = compraCriteria.copy();

        assertThat(compraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(compraCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var compraCriteria = new CompraCriteria();

        assertThat(compraCriteria).hasToString("CompraCriteria{}");
    }

    private static void setAllFilters(CompraCriteria compraCriteria) {
        compraCriteria.id();
        compraCriteria.fechaCompra();
        compraCriteria.total();
        compraCriteria.proveedorId();
        compraCriteria.distinct();
    }

    private static Condition<CompraCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaCompra()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getProveedorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CompraCriteria> copyFiltersAre(CompraCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaCompra(), copy.getFechaCompra()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getProveedorId(), copy.getProveedorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
