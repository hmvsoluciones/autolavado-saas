package com.hmvsoluciones.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClienteCriteriaTest {

    @Test
    void newClienteCriteriaHasAllFiltersNullTest() {
        var clienteCriteria = new ClienteCriteria();
        assertThat(clienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clienteCriteriaFluentMethodsCreatesFiltersTest() {
        var clienteCriteria = new ClienteCriteria();

        setAllFilters(clienteCriteria);

        assertThat(clienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clienteCriteriaCopyCreatesNullFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void clienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        setAllFilters(clienteCriteria);

        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clienteCriteria = new ClienteCriteria();

        assertThat(clienteCriteria).hasToString("ClienteCriteria{}");
    }

    private static void setAllFilters(ClienteCriteria clienteCriteria) {
        clienteCriteria.id();
        clienteCriteria.nombre();
        clienteCriteria.email();
        clienteCriteria.telefono();
        clienteCriteria.razonSocial();
        clienteCriteria.rfc();
        clienteCriteria.activo();
        clienteCriteria.ventaId();
        clienteCriteria.distinct();
    }

    private static Condition<ClienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getRazonSocial()) &&
                condition.apply(criteria.getRfc()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getVentaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClienteCriteria> copyFiltersAre(ClienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getRazonSocial(), copy.getRazonSocial()) &&
                condition.apply(criteria.getRfc(), copy.getRfc()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getVentaId(), copy.getVentaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
