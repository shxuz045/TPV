package com.sonia.tpv.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean disponible = true;

    @Column(length = 50)
    private String categoria = "GENERAL";

    @Column(name = "orden")
    private Integer orden = 0;

    public Producto() {
    }

    public Producto(String nombre, BigDecimal precio, String categoria, boolean disponible, Integer orden) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
        this.orden = (orden != null ? orden : 0);
    }

    public Producto(String nombre, BigDecimal precio, String categoria, Integer orden) {
        this(nombre, precio, categoria, true, orden);
    }

    public Producto(String nombre, BigDecimal precio, String categoria, boolean disponible) {
        this(nombre, precio, categoria, disponible, 0);
    }

    public Producto(String nombre, BigDecimal precio, String categoria) {
        this(nombre, precio, categoria, true, 0);
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }

    public String getNumero() {
        if (nombre == null) return "";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^\\s*([A-Za-z]*\\d+[A-Za-z]*)\\s*\\.?").matcher(nombre);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public String getNombreSinNumero() {
        if (nombre == null) return "";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^\\s*([A-Za-z]*\\d+[A-Za-z]*)\\s*\\.?\\s*(.*)$").matcher(nombre);
        if (m.find()) {
            return m.group(2);
        }
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", disponible=" + disponible +
                ", orden=" + orden +
                '}';
    }
}
