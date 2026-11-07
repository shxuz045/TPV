package com.sonia.tpv.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes_domicilio")
public class ClienteDomicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String telefono;

    @Column(length = 100)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime ultimaVisita;

    // Lista de direcciones guardadas para este cliente
    @ElementCollection
    @CollectionTable(name = "cliente_direcciones", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "direccion", length = 250)
    @OrderColumn(name = "posicion")
    private List<String> direcciones = new ArrayList<>();

    public ClienteDomicilio() {}

    public Long getId() { return id; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getUltimaVisita() { return ultimaVisita; }
    public void setUltimaVisita(LocalDateTime ultimaVisita) { this.ultimaVisita = ultimaVisita; }
    public List<String> getDirecciones() { return direcciones; }
    public void setDirecciones(List<String> direcciones) { this.direcciones = direcciones; }
}

