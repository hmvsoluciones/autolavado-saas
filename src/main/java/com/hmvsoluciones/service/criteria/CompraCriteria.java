package com.hmvsoluciones.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.domain.Compra} entity. This class is used
 * in {@link com.hmvsoluciones.web.rest.CompraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaCompra;

    private BigDecimalFilter total;

    private LongFilter proveedorId;

    private Boolean distinct;

    public CompraCriteria() {}

    public CompraCriteria(CompraCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaCompra = other.optionalFechaCompra().map(LocalDateFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(BigDecimalFilter::copy).orElse(null);
        this.proveedorId = other.optionalProveedorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CompraCriteria copy() {
        return new CompraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFechaCompra() {
        return fechaCompra;
    }

    public Optional<LocalDateFilter> optionalFechaCompra() {
        return Optional.ofNullable(fechaCompra);
    }

    public LocalDateFilter fechaCompra() {
        if (fechaCompra == null) {
            setFechaCompra(new LocalDateFilter());
        }
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateFilter fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimalFilter getTotal() {
        return total;
    }

    public Optional<BigDecimalFilter> optionalTotal() {
        return Optional.ofNullable(total);
    }

    public BigDecimalFilter total() {
        if (total == null) {
            setTotal(new BigDecimalFilter());
        }
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public LongFilter getProveedorId() {
        return proveedorId;
    }

    public Optional<LongFilter> optionalProveedorId() {
        return Optional.ofNullable(proveedorId);
    }

    public LongFilter proveedorId() {
        if (proveedorId == null) {
            setProveedorId(new LongFilter());
        }
        return proveedorId;
    }

    public void setProveedorId(LongFilter proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompraCriteria that = (CompraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaCompra, that.fechaCompra) &&
            Objects.equals(total, that.total) &&
            Objects.equals(proveedorId, that.proveedorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaCompra, total, proveedorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaCompra().map(f -> "fechaCompra=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalProveedorId().map(f -> "proveedorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
