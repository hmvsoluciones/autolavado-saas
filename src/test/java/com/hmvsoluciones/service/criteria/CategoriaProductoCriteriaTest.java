package com.hmvsoluciones.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CategoriaProductoCriteriaTest {

    @Test
    void newCategoriaProductoCriteriaHasAllFiltersNullTest() {
        var categoriaProductoCriteria = new CategoriaProductoCriteria();
        assertThat(categoriaProductoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void categoriaProductoCriteriaFluentMethodsCreatesFiltersTest() {
        var categoriaProductoCriteria = new CategoriaProductoCriteria();

        setAllFilters(categoriaProductoCriteria);

        assertThat(categoriaProductoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void categoriaProductoCriteriaCopyCreatesNullFilterTest() {
        var categoriaProductoCriteria = new CategoriaProductoCriteria();
        var copy = categoriaProductoCriteria.copy();

        assertThat(categoriaProductoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(categoriaProductoCriteria)
        );
    }

    @Test
    void categoriaProductoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var categoriaProductoCriteria = new CategoriaProductoCriteria();
        setAllFilters(categoriaProductoCriteria);

        var copy = categoriaProductoCriteria.copy();

        assertThat(categoriaProductoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(categoriaProductoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var categoriaProductoCriteria = new CategoriaProductoCriteria();

        assertThat(categoriaProductoCriteria).hasToString("CategoriaProductoCriteria{}");
    }

    private static void setAllFilters(CategoriaProductoCriteria categoriaProductoCriteria) {
        categoriaProductoCriteria.id();
        categoriaProductoCriteria.nombre();
        categoriaProductoCriteria.descripcion();
        categoriaProductoCriteria.productoId();
        categoriaProductoCriteria.distinct();
    }

    private static Condition<CategoriaProductoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CategoriaProductoCriteria> copyFiltersAre(
        CategoriaProductoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
