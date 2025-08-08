package com.hmvsoluciones.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.domain.DetalleVenta} entity. This class is used
 * in {@link com.hmvsoluciones.web.rest.DetalleVentaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /detalle-ventas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleVentaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cantidad;

    private BigDecimalFilter precioUnitario;

    private BigDecimalFilter subtotal;

    private LongFilter ventaId;

    private LongFilter productoId;

    private Boolean distinct;

    public DetalleVentaCriteria() {}

    public DetalleVentaCriteria(DetalleVentaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.precioUnitario = other.optionalPrecioUnitario().map(BigDecimalFilter::copy).orElse(null);
        this.subtotal = other.optionalSubtotal().map(BigDecimalFilter::copy).orElse(null);
        this.ventaId = other.optionalVentaId().map(LongFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DetalleVentaCriteria copy() {
        return new DetalleVentaCriteria(this);
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

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public Optional<IntegerFilter> optionalCantidad() {
        return Optional.ofNullable(cantidad);
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            setCantidad(new IntegerFilter());
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimalFilter getPrecioUnitario() {
        return precioUnitario;
    }

    public Optional<BigDecimalFilter> optionalPrecioUnitario() {
        return Optional.ofNullable(precioUnitario);
    }

    public BigDecimalFilter precioUnitario() {
        if (precioUnitario == null) {
            setPrecioUnitario(new BigDecimalFilter());
        }
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimalFilter precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimalFilter getSubtotal() {
        return subtotal;
    }

    public Optional<BigDecimalFilter> optionalSubtotal() {
        return Optional.ofNullable(subtotal);
    }

    public BigDecimalFilter subtotal() {
        if (subtotal == null) {
            setSubtotal(new BigDecimalFilter());
        }
        return subtotal;
    }

    public void setSubtotal(BigDecimalFilter subtotal) {
        this.subtotal = subtotal;
    }

    public LongFilter getVentaId() {
        return ventaId;
    }

    public Optional<LongFilter> optionalVentaId() {
        return Optional.ofNullable(ventaId);
    }

    public LongFilter ventaId() {
        if (ventaId == null) {
            setVentaId(new LongFilter());
        }
        return ventaId;
    }

    public void setVentaId(LongFilter ventaId) {
        this.ventaId = ventaId;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public Optional<LongFilter> optionalProductoId() {
        return Optional.ofNullable(productoId);
    }

    public LongFilter productoId() {
        if (productoId == null) {
            setProductoId(new LongFilter());
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
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
        final DetalleVentaCriteria that = (DetalleVentaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(precioUnitario, that.precioUnitario) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(ventaId, that.ventaId) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, precioUnitario, subtotal, ventaId, productoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVentaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalPrecioUnitario().map(f -> "precioUnitario=" + f + ", ").orElse("") +
            optionalSubtotal().map(f -> "subtotal=" + f + ", ").orElse("") +
            optionalVentaId().map(f -> "ventaId=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
