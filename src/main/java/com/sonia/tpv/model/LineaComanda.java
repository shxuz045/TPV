package com.sonia.tpv.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lineas_comanda")
public class LineaComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(nullable = false)
    private int cantidad = 1;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(length = 1000)
    private String notas;

    public LineaComanda() {
    }

    public LineaComanda(Comanda comanda, Producto producto, int cantidad, BigDecimal precioUnitario, String notas) {
        this.comanda = comanda;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.notas = notas;
    }

    public LineaComanda(Producto producto, int cantidad, BigDecimal precioUnitario) {
        this(null, producto, cantidad, precioUnitario, null);
    }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getId() { return id; }

    public Comanda getComanda() { return comanda; }
    public void setComanda(Comanda comanda) { this.comanda = comanda; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return "LineaComanda{" +
                "id=" + id +
                ", producto=" + (producto != null ? producto.getNombre() : null) +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
