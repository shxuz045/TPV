package com.sonia.tpv.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonia.tpv.model.Comanda;
import com.sonia.tpv.model.EstadoComanda;
import com.sonia.tpv.model.FaseDomicilio;
import com.sonia.tpv.model.LineaComanda;
import com.sonia.tpv.model.OrigenPedido;
import com.sonia.tpv.model.Producto;
import com.sonia.tpv.model.TipoServicio;
import com.sonia.tpv.model.Usuario;
import com.sonia.tpv.repository.ComandaRepository;
import com.sonia.tpv.repository.ProductoRepository;
import com.sonia.tpv.repository.UsuarioRepository;

@Service
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public ComandaService(ComandaRepository comandaRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {
        this.comandaRepository = comandaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Comanda> listarAbiertas() {
        return comandaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoComanda.ABIERTA).stream()
                .filter(c -> c.getLineas() != null && !c.getLineas().isEmpty())
                .toList();
    }

    @Transactional
    public void limpiarComandasVacias() {
        List<Comanda> abiertas = comandaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoComanda.ABIERTA);
        for (Comanda c : abiertas) {
            if (c.getTipoServicio() != TipoServicio.DOMICILIO
                    && (c.getLineas() == null || c.getLineas().isEmpty())
                    && c.getFechaCreacion() != null
                    && c.getFechaCreacion().isBefore(LocalDateTime.now().minusMinutes(1))) {
                comandaRepository.delete(c);
            }
        }
    }

    public Comanda obtener(Long id) {
        return comandaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comanda no encontrada: " + id));
    }

    public Optional<Comanda> buscarAbiertaPorMesa(String mesa) {
        if (mesa == null || mesa.isBlank()) {
            return Optional.empty();
        }
        String mesaNormalizada = normalizarMesa(mesa);
        return comandaRepository.findFirstByMesaAndEstado(mesaNormalizada, EstadoComanda.ABIERTA);
    }

    @Transactional
    public Comanda obtenerOCrearParaMesa(String mesa, TipoServicio tipoServicio, String usernameCajero) {
        String mesaNormalizada = normalizarMesa(mesa);
        TipoServicio tipo = tipoServicio != null ? tipoServicio : TipoServicio.SALA;

        // Regla: cada mesa solo puede tener una comanda abierta
        List<Comanda> abiertas = comandaRepository.findByMesaAndEstado(mesaNormalizada, EstadoComanda.ABIERTA);
        for (Comanda c : abiertas) {
            if (c.getLineas() != null && !c.getLineas().isEmpty()) {
                return c;
            }
        }

        if (!abiertas.isEmpty()) {
            return abiertas.get(0);
        }

        return crear(mesaNormalizada, tipo, null, usernameCajero);
    }

    private String normalizarMesa(String mesa) {
        if (mesa == null || mesa.isBlank()) {
            return "Mesa 1";
        }
        String m = mesa.trim();
        if (m.matches("^\\d+$")) {
            return "Mesa " + m;
        }
        return m;
    }

    @Transactional
    public Comanda crear(String mesa, TipoServicio tipoServicio, String clienteInfo, String usernameCajero) {
        Usuario cajero = usuarioRepository.findByUsername(usernameCajero)
                .orElseThrow(() -> new IllegalArgumentException("Cajero no encontrado"));

        String mesaFinal = normalizarMesa(mesa);
        TipoServicio tipoFinal = tipoServicio != null ? tipoServicio : TipoServicio.SALA;

        // Regla: si ya hay comanda abierta para esta mesa o recogida, devolver la
        // existente
        if (mesaFinal != null && !mesaFinal.isBlank()) {
            Optional<Comanda> existente = comandaRepository.findFirstByMesaAndEstado(mesaFinal, EstadoComanda.ABIERTA);
            if (existente.isPresent()) {
                return existente.get();
            }
        }

        Comanda comanda = new Comanda();
        comanda.setMesa(mesaFinal);
        comanda.setTipoServicio(tipoFinal);
        comanda.setClienteInfo(clienteInfo);
        comanda.setCajero(cajero);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setFechaCreacion(LocalDateTime.now());
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda actualizarEncabezado(Long comandaId, TipoServicio tipoServicio, String mesa, String clienteInfo) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda no está abierta");
        }
        if (tipoServicio != null) {
            comanda.setTipoServicio(tipoServicio);
        }
        if (mesa != null) {
            comanda.setMesa(mesa);
        }
        if (clienteInfo != null) {
            comanda.setClienteInfo(clienteInfo);
        }
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda anadirProducto(Long comandaId, Long productoId, int cantidad) {
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }

        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("Solo se pueden editar comandas abiertas");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (!producto.isDisponible()) {
            throw new IllegalStateException("El producto no está disponible");
        }

        for (LineaComanda linea : comanda.getLineas()) {
            if (linea.getProducto().getId().equals(productoId)) {
                linea.setCantidad(linea.getCantidad() + cantidad);
                return comandaRepository.save(comanda);
            }
        }

        LineaComanda linea = new LineaComanda();
        linea.setProducto(producto);
        linea.setCantidad(cantidad);
        linea.setPrecioUnitario(producto.getPrecio());
        comanda.addLinea(linea);
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda cambiarCantidadLinea(Long comandaId, Long lineaId, int cantidad) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda no está abierta");
        }

        if (cantidad <= 0) {
            comanda.getLineas().removeIf(l -> l.getId().equals(lineaId));
        } else {
            for (LineaComanda linea : comanda.getLineas()) {
                if (linea.getId().equals(lineaId)) {
                    linea.setCantidad(cantidad);
                    break;
                }
            }
        }
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda eliminarLinea(Long comandaId, Long lineaId) {
        return cambiarCantidadLinea(comandaId, lineaId, 0);
    }

    @Transactional
    public Comanda anadirNotaLinea(Long comandaId, Long lineaId, String nota) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda no está abierta");
        }
        for (LineaComanda linea : comanda.getLineas()) {
            if (linea.getId().equals(lineaId)) {
                linea.setNotas(nota);
                break;
            }
        }
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda vaciarLineas(Long comandaId) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda no está abierta");
        }
        comanda.getLineas().clear();
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda cobrarYCerrar(Long comandaId, String metodoPago, BigDecimal importeEntregado) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda ya no está abierta");
        }
        if (comanda.getLineas().isEmpty()) {
            throw new IllegalStateException("No se puede cerrar una comanda vacía");
        }

        BigDecimal total = comanda.getTotal();
        comanda.setMetodoPago(metodoPago != null ? metodoPago : "EFECTIVO");

        if (importeEntregado != null && importeEntregado.compareTo(total) >= 0) {
            comanda.setImporteEntregado(importeEntregado);
            comanda.setCambio(importeEntregado.subtract(total));
        } else {
            comanda.setImporteEntregado(total);
            comanda.setCambio(BigDecimal.ZERO);
        }

        comanda.setEstado(EstadoComanda.CERRADA);
        comanda.setFechaCierre(LocalDateTime.now());
        if (comanda.getTipoServicio() == TipoServicio.DOMICILIO) {
            comanda.setFaseDomicilio(FaseDomicilio.ENTREGADO);
        }
        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda cancelar(Long comandaId) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("Solo se pueden cancelar comandas abiertas");
        }
        comanda.setEstado(EstadoComanda.CANCELADA);
        comanda.setFechaCierre(LocalDateTime.now());
        return comandaRepository.save(comanda);
    }

    /**
     * Mueve una comanda de SALA a RECOGIDA.
     * Asigna automáticamente el primer número de recogida libre (a partir de 40).
     */
    @Transactional
    public Comanda moverARecogida(Long comandaId) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda no está abierta");
        }

        // Buscar el primer número de recogida libre (40-89)
        List<Comanda> abiertas = comandaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoComanda.ABIERTA);
        java.util.Set<String> recogidas = abiertas.stream()
                .filter(c -> !c.getId().equals(comandaId) && c.getLineas() != null && !c.getLineas().isEmpty())
                .map(Comanda::getMesa)
                .filter(m -> m != null && m.startsWith("Recogida "))
                .collect(java.util.stream.Collectors.toSet());

        String nuevaMesa = null;
        for (int i = 40; i <= 89; i++) {
            String candidato = "Recogida " + i;
            if (!recogidas.contains(candidato)) {
                nuevaMesa = candidato;
                break;
            }
        }
        if (nuevaMesa == null) {
            throw new IllegalStateException("No hay números de recogida disponibles (40-89 ocupados)");
        }

        comanda.setTipoServicio(TipoServicio.RECOGIDA);
        comanda.setMesa(nuevaMesa);
        return comandaRepository.save(comanda);
    }

    /**
     * Mueve una comanda a otra mesa libre.
     * Si la mesa destino ya tiene una comanda abierta con artículos, lanza
     * excepción.
     */
    @Transactional
    public Comanda moverAMesa(Long comandaId, String nuevaMesa) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getEstado() != EstadoComanda.ABIERTA) {
            throw new IllegalStateException("La comanda no está abierta");
        }

        String mesaNormalizada = normalizarMesa(nuevaMesa);

        // Verificar que la mesa destino está libre
        List<Comanda> enDestino = comandaRepository.findByMesaAndEstado(mesaNormalizada, EstadoComanda.ABIERTA);
        for (Comanda c : enDestino) {
            if (!c.getId().equals(comandaId) && c.getLineas() != null && !c.getLineas().isEmpty()) {
                throw new IllegalStateException(
                        "La mesa " + mesaNormalizada + " ya tiene una comanda abierta con artículos");
            }
        }
        // Eliminar comandas vacías en destino
        for (Comanda c : enDestino) {
            if (!c.getId().equals(comandaId)) {
                comandaRepository.delete(c);
            }
        }

        comanda.setMesa(mesaNormalizada);
        return comandaRepository.save(comanda);
    }

    /**
     * Crea un nuevo pedido para Servicio a Domicilio con datos de entrega y origen.
     */
    @Transactional
    public Comanda crearDomicilio(String nombreCliente,
            String telefono,
            String direccion,
            String notasEntrega,
            String horaEntrega,
            String usernameCajero) {
        Usuario cajero = usuarioRepository.findByUsername(usernameCajero)
                .orElseThrow(() -> new IllegalArgumentException("Cajero no encontrado"));

        // Buscar el primer número sobrante libre a partir del 90 (rango 90-99 reservado
        // para domicilios)
        List<Comanda> abiertas = comandaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoComanda.ABIERTA);
        java.util.Set<String> ocupados = abiertas.stream()
                .filter(c -> c.getTipoServicio() == TipoServicio.DOMICILIO)
                .map(Comanda::getMesa)
                .filter(m -> m != null)
                .collect(java.util.stream.Collectors.toSet());

        String nuevoNumero = null;
        for (int i = 90; i <= 99; i++) {
            String candidato = "Domicilio " + i;
            if (!ocupados.contains(candidato) && !ocupados.contains("" + i)) {
                nuevoNumero = candidato;
                break;
            }
        }
        if (nuevoNumero == null) {
            nuevoNumero = "Domicilio " + (90 + ocupados.size());
        }

        Comanda comanda = new Comanda();
        comanda.setTipoServicio(TipoServicio.DOMICILIO);
        comanda.setMesa(nuevoNumero);
        comanda.setNombreCliente(nombreCliente != null ? nombreCliente.trim() : "Cliente");
        comanda.setTelefonoCliente(telefono != null ? telefono.trim() : "");
        comanda.setDireccionCliente(direccion != null ? direccion.trim() : "");
        comanda.setNotasEntrega(notasEntrega != null ? notasEntrega.trim() : "");
        comanda.setOrigenPedido(OrigenPedido.TELEFONO);
        comanda.setFaseDomicilio(FaseDomicilio.PENDIENTE);
        comanda.setHoraEntregaProgramada(
                horaEntrega != null && !horaEntrega.isBlank() ? horaEntrega.trim() : "Al momento");
        comanda.setCajero(cajero);
        comanda.setEstado(EstadoComanda.ABIERTA);
        comanda.setFechaCreacion(LocalDateTime.now());

        return comandaRepository.save(comanda);
    }

    @Transactional
    public Comanda crearDomicilio(String nombreCliente,
            String telefono,
            String direccion,
            String notasEntrega,
            OrigenPedido origen,
            String horaEntrega,
            String usernameCajero) {
        return crearDomicilio(nombreCliente, telefono, direccion, notasEntrega, horaEntrega, usernameCajero);
    }

    /**
     * Avanza o cambia de fase un pedido de Domicilio.
     */
    @Transactional
    public Comanda cambiarFaseDomicilio(Long comandaId, FaseDomicilio nuevaFase, String repartidor) {
        Comanda comanda = obtener(comandaId);
        if (comanda.getTipoServicio() != TipoServicio.DOMICILIO) {
            throw new IllegalStateException("Esta comanda no es de servicio a domicilio");
        }
        if (nuevaFase != null) {
            comanda.setFaseDomicilio(nuevaFase);
        }
        if (repartidor != null && !repartidor.isBlank()) {
            comanda.setRepartidor(repartidor.trim());
        }
        if (nuevaFase == FaseDomicilio.ENTREGADO && comanda.getEstado() == EstadoComanda.ABIERTA) {
            // Si pasa a entregado sin haber cobrado aún explícitamente, registramos cobro
            // automático si no tenía
            if (comanda.getMetodoPago() == null) {
                comanda.setMetodoPago("EFECTIVO");
            }
            comanda.setEstado(EstadoComanda.CERRADA);
            comanda.setFechaCierre(LocalDateTime.now());
        }
        return comandaRepository.save(comanda);
    }

    /**
     * Lista todos los pedidos de Domicilio ordenados por fecha de creación
     * descendente.
     */
    public List<Comanda> listarDomicilios() {
        return comandaRepository.findByTipoServicioOrderByFechaCreacionDesc(TipoServicio.DOMICILIO);
    }

}
