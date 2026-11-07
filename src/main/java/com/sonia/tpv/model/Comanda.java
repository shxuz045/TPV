package com.sonia.tpv.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "comandas")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String mesa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoComanda estado = EstadoComanda.ABIERTA;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime fechaCierre;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean ticketImpreso = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoServicio tipoServicio = TipoServicio.SALA;

    @Column(length = 200)
    private String clienteInfo;

    // Campos específicos para servicio a domicilio
    @Column(length = 100)
    private String nombreCliente;

    @Column(length = 30)
    private String telefonoCliente;

    @Column(length = 250)
    private String direccionCliente;

    @Column(length = 250)
    private String notasEntrega;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrigenPedido origenPedido = OrigenPedido.TELEFONO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FaseDomicilio faseDomicilio = FaseDomicilio.PENDIENTE;

    @Column(length = 50)
    private String horaEntregaProgramada;

    @Column(length = 80)
    private String repartidor;

    @Column(length = 30)
    private String metodoPago;

    private BigDecimal importeEntregado;
    private BigDecimal cambio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cajero_id")
    private Usuario cajero;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaComanda> lineas = new ArrayList<>();

    public Comanda() {
    }

    public Comanda(String mesa, TipoServicio tipoServicio, String clienteInfo, Usuario cajero) {
        this.mesa = mesa;
        this.tipoServicio = tipoServicio != null ? tipoServicio : TipoServicio.SALA;
        this.clienteInfo = clienteInfo;
        this.cajero = cajero;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoComanda.ABIERTA;
    }

    public BigDecimal getTotal() {
        if (lineas == null) {
            return BigDecimal.ZERO;
        }
        return lineas.stream()
                .map(l -> l != null && l.getSubtotal() != null ? l.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addLinea(LineaComanda linea) {
        lineas.add(linea);
        linea.setComanda(this);
    }

    public Long getId() { return id; }

    public String getMesa() { return mesa; }
    public void setMesa(String mesa) { this.mesa = mesa; }

    public EstadoComanda getEstado() { return estado; }
    public void setEstado(EstadoComanda estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public boolean isTicketImpreso() { return ticketImpreso; }
    public void setTicketImpreso(boolean ticketImpreso) { this.ticketImpreso = ticketImpreso; }

    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getClienteInfo() { return clienteInfo; }
    public void setClienteInfo(String clienteInfo) { this.clienteInfo = clienteInfo; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getDireccionCliente() { return direccionCliente; }
    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }

    public String getNotasEntrega() { return notasEntrega; }
    public void setNotasEntrega(String notasEntrega) { this.notasEntrega = notasEntrega; }

    public OrigenPedido getOrigenPedido() { return origenPedido; }
    public void setOrigenPedido(OrigenPedido origenPedido) { this.origenPedido = origenPedido; }

    public FaseDomicilio getFaseDomicilio() { return faseDomicilio; }
    public void setFaseDomicilio(FaseDomicilio faseDomicilio) { this.faseDomicilio = faseDomicilio; }

    public String getHoraEntregaProgramada() { return horaEntregaProgramada; }
    public void setHoraEntregaProgramada(String horaEntregaProgramada) { this.horaEntregaProgramada = horaEntregaProgramada; }

    public String getRepartidor() { return repartidor; }
    public void setRepartidor(String repartidor) { this.repartidor = repartidor; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public BigDecimal getImporteEntregado() { return importeEntregado; }
    public void setImporteEntregado(BigDecimal importeEntregado) { this.importeEntregado = importeEntregado; }

    public BigDecimal getCambio() { return cambio; }
    public void setCambio(BigDecimal cambio) { this.cambio = cambio; }

    public Usuario getCajero() { return cajero; }
    public void setCajero(Usuario cajero) { this.cajero = cajero; }

    public List<LineaComanda> getLineas() { return lineas; }
    public void setLineas(List<LineaComanda> lineas) { this.lineas = lineas; }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", mesa='" + mesa + '\'' +
                ", estado=" + estado +
                ", tipoServicio=" + tipoServicio +
                ", fechaCreacion=" + fechaCreacion +
                ", total=" + getTotal() +
                '}';
    }
}
