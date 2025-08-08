package com.hmvsoluciones.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", precision = 21, scale = 2, nullable = false)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ventas" }, allowSetters = true)
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta")
    @JsonIgnoreProperties(value = { "venta", "producto" }, allowSetters = true)
    private Set<DetalleVenta> detalleVentas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta")
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Factura> facturas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaVenta() {
        return this.fechaVenta;
    }

    public Venta fechaVenta(LocalDate fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Venta total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Venta cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Set<DetalleVenta> getDetalleVentas() {
        return this.detalleVentas;
    }

    public void setDetalleVentas(Set<DetalleVenta> detalleVentas) {
        if (this.detalleVentas != null) {
            this.detalleVentas.forEach(i -> i.setVenta(null));
        }
        if (detalleVentas != null) {
            detalleVentas.forEach(i -> i.setVenta(this));
        }
        this.detalleVentas = detalleVentas;
    }

    public Venta detalleVentas(Set<DetalleVenta> detalleVentas) {
        this.setDetalleVentas(detalleVentas);
        return this;
    }

    public Venta addDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVentas.add(detalleVenta);
        detalleVenta.setVenta(this);
        return this;
    }

    public Venta removeDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVentas.remove(detalleVenta);
        detalleVenta.setVenta(null);
        return this;
    }

    public Set<Factura> getFacturas() {
        return this.facturas;
    }

    public void setFacturas(Set<Factura> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setVenta(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setVenta(this));
        }
        this.facturas = facturas;
    }

    public Venta facturas(Set<Factura> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Venta addFactura(Factura factura) {
        this.facturas.add(factura);
        factura.setVenta(this);
        return this;
    }

    public Venta removeFactura(Factura factura) {
        this.facturas.remove(factura);
        factura.setVenta(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return getId() != null && getId().equals(((Venta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
