package com.hmvsoluciones.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.domain.Producto} entity. This class is used
 * in {@link com.hmvsoluciones.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private BigDecimalFilter precio;

    private IntegerFilter cantidad;

    private LocalDateFilter fechaActualizacion;

    private BooleanFilter activo;

    private LongFilter categoriaProductoId;

    private LongFilter detalleVentaId;

    private Boolean distinct;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.precio = other.optionalPrecio().map(BigDecimalFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.fechaActualizacion = other.optionalFechaActualizacion().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.categoriaProductoId = other.optionalCategoriaProductoId().map(LongFilter::copy).orElse(null);
        this.detalleVentaId = other.optionalDetalleVentaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimalFilter getPrecio() {
        return precio;
    }

    public Optional<BigDecimalFilter> optionalPrecio() {
        return Optional.ofNullable(precio);
    }

    public BigDecimalFilter precio() {
        if (precio == null) {
            setPrecio(new BigDecimalFilter());
        }
        return precio;
    }

    public void setPrecio(BigDecimalFilter precio) {
        this.precio = precio;
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

    public LocalDateFilter getFechaActualizacion() {
        return fechaActualizacion;
    }

    public Optional<LocalDateFilter> optionalFechaActualizacion() {
        return Optional.ofNullable(fechaActualizacion);
    }

    public LocalDateFilter fechaActualizacion() {
        if (fechaActualizacion == null) {
            setFechaActualizacion(new LocalDateFilter());
        }
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateFilter fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
    }

    public LongFilter getCategoriaProductoId() {
        return categoriaProductoId;
    }

    public Optional<LongFilter> optionalCategoriaProductoId() {
        return Optional.ofNullable(categoriaProductoId);
    }

    public LongFilter categoriaProductoId() {
        if (categoriaProductoId == null) {
            setCategoriaProductoId(new LongFilter());
        }
        return categoriaProductoId;
    }

    public void setCategoriaProductoId(LongFilter categoriaProductoId) {
        this.categoriaProductoId = categoriaProductoId;
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
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(fechaActualizacion, that.fechaActualizacion) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(categoriaProductoId, that.categoriaProductoId) &&
            Objects.equals(detalleVentaId, that.detalleVentaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            descripcion,
            precio,
            cantidad,
            fechaActualizacion,
            activo,
            categoriaProductoId,
            detalleVentaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalPrecio().map(f -> "precio=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalFechaActualizacion().map(f -> "fechaActualizacion=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalCategoriaProductoId().map(f -> "categoriaProductoId=" + f + ", ").orElse("") +
            optionalDetalleVentaId().map(f -> "detalleVentaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
