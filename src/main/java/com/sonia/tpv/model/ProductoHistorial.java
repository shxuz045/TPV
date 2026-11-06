package com.sonia.tpv.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto_historial")
public class ProductoHistorial {

    public enum Accion {
        CREAR, MODIFICAR, ELIMINAR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID del producto afectado (puede ya no existir si fue eliminado) */
    private Long productoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Accion accion;

    /** Valores del producto en el momento del cambio */
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 50)
    private String categoria;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public ProductoHistorial() {
    }

    public ProductoHistorial(Long productoId, Accion accion, String nombre, BigDecimal precio, String categoria) {
        this.productoId = productoId;
        this.accion = accion;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Accion getAccion() { return accion; }
    public void setAccion(Accion accion) { this.accion = accion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

