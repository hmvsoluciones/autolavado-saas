package com.hmvsoluciones.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacturaCriteriaTest {

    @Test
    void newFacturaCriteriaHasAllFiltersNullTest() {
        var facturaCriteria = new FacturaCriteria();
        assertThat(facturaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facturaCriteriaFluentMethodsCreatesFiltersTest() {
        var facturaCriteria = new FacturaCriteria();

        setAllFilters(facturaCriteria);

        assertThat(facturaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facturaCriteriaCopyCreatesNullFilterTest() {
        var facturaCriteria = new FacturaCriteria();
        var copy = facturaCriteria.copy();

        assertThat(facturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facturaCriteria)
        );
    }

    @Test
    void facturaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facturaCriteria = new FacturaCriteria();
        setAllFilters(facturaCriteria);

        var copy = facturaCriteria.copy();

        assertThat(facturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facturaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facturaCriteria = new FacturaCriteria();

        assertThat(facturaCriteria).hasToString("FacturaCriteria{}");
    }

    private static void setAllFilters(FacturaCriteria facturaCriteria) {
        facturaCriteria.id();
        facturaCriteria.numero();
        facturaCriteria.fechaEmision();
        facturaCriteria.total();
        facturaCriteria.activo();
        facturaCriteria.ventaId();
        facturaCriteria.distinct();
    }

    private static Condition<FacturaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getFechaEmision()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getVentaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacturaCriteria> copyFiltersAre(FacturaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getFechaEmision(), copy.getFechaEmision()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getVentaId(), copy.getVentaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
