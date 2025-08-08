package com.hmvsoluciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DetalleVenta.
 */
@Entity
@Table(name = "detalle_venta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_unitario", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", precision = 21, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cliente", "detalleVentas", "facturas" }, allowSetters = true)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "categoriaProducto", "detalleVentas" }, allowSetters = true)
    private Producto producto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public DetalleVenta cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return this.precioUnitario;
    }

    public DetalleVenta precioUnitario(BigDecimal precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public DetalleVenta subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DetalleVenta venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public DetalleVenta producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleVenta)) {
            return false;
        }
        return getId() != null && getId().equals(((DetalleVenta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVenta{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", subtotal=" + getSubtotal() +
            "}";
    }
}
