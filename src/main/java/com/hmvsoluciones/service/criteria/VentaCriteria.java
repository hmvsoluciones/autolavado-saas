package com.hmvsoluciones.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.domain.Venta} entity. This class is used
 * in {@link com.hmvsoluciones.web.rest.VentaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ventas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaVenta;

    private BigDecimalFilter total;

    private LongFilter clienteId;

    private LongFilter detalleVentaId;

    private LongFilter facturaId;

    private Boolean distinct;

    public VentaCriteria() {}

    public VentaCriteria(VentaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaVenta = other.optionalFechaVenta().map(LocalDateFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(BigDecimalFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.detalleVentaId = other.optionalDetalleVentaId().map(LongFilter::copy).orElse(null);
        this.facturaId = other.optionalFacturaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VentaCriteria copy() {
        return new VentaCriteria(this);
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

    public LocalDateFilter getFechaVenta() {
        return fechaVenta;
    }

    public Optional<LocalDateFilter> optionalFechaVenta() {
        return Optional.ofNullable(fechaVenta);
    }

    public LocalDateFilter fechaVenta() {
        if (fechaVenta == null) {
            setFechaVenta(new LocalDateFilter());
        }
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateFilter fechaVenta) {
        this.fechaVenta = fechaVenta;
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

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public LongFilter getDetalleVentaId() {
        return detalleVentaId;
    }

    public Optional<LongFilter> optionalDetalleVentaId() {
        return Optional.ofNullable(detalleVentaId);
    }

    public LongFilter detalleVentaId() {
        if (detalleVentaId == null) {
            setDetalleVentaId(new LongFilter());
        }
        return detalleVentaId;
    }

    public void setDetalleVentaId(LongFilter detalleVentaId) {
        this.detalleVentaId = detalleVentaId;
    }

    public LongFilter getFacturaId() {
        return facturaId;
    }

    public Optional<LongFilter> optionalFacturaId() {
        return Optional.ofNullable(facturaId);
    }

    public LongFilter facturaId() {
        if (facturaId == null) {
            setFacturaId(new LongFilter());
        }
        return facturaId;
    }

    public void setFacturaId(LongFilter facturaId) {
        this.facturaId = facturaId;
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
        final VentaCriteria that = (VentaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaVenta, that.fechaVenta) &&
            Objects.equals(total, that.total) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(detalleVentaId, that.detalleVentaId) &&
            Objects.equals(facturaId, that.facturaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaVenta, total, clienteId, detalleVentaId, facturaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaVenta().map(f -> "fechaVenta=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDetalleVentaId().map(f -> "detalleVentaId=" + f + ", ").orElse("") +
            optionalFacturaId().map(f -> "facturaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
