package com.sonia.tpv.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sonia.tpv.model.Comanda;
import com.sonia.tpv.model.Producto;
import com.sonia.tpv.model.TipoServicio;
import com.sonia.tpv.repository.ProductoRepository;
import com.sonia.tpv.service.ComandaService;
import com.sonia.tpv.util.ProductoComparator;

@RestController
@RequestMapping("/api/pos")
public class ApiComandaController {

    private final ComandaService comandaService;
    private final ProductoRepository productoRepository;

    public ApiComandaController(ComandaService comandaService, ProductoRepository productoRepository) {
        this.comandaService = comandaService;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/data")
    public ResponseEntity<?> getPosData(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productos = productoRepository.findByDisponibleTrueOrderByOrdenAsc();
        productos.sort(new ProductoComparator());
        response.put("productos", productos);
        response.put("comandasAbiertas", comandaService.listarAbiertas());
        response.put("cajero", user != null ? user.getUsername() : "admin");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearComanda(@RequestParam(required = false, defaultValue = "Mesa 1") String mesa,
                                          @RequestParam(required = false, defaultValue = "SALA") String tipoServicio,
                                          @RequestParam(required = false) String clienteInfo,
                                          @AuthenticationPrincipal UserDetails user) {
        TipoServicio tipo = TipoServicio.valueOf(tipoServicio.toUpperCase());
        Comanda comanda = comandaService.crear(mesa, tipo, clienteInfo, user != null ? user.getUsername() : "admin");
        return ResponseEntity.ok(comanda);
    }

    @PostMapping("/{id}/lineas")
    public ResponseEntity<?> anadirLinea(@PathVariable Long id,
                                         @RequestParam Long productoId,
                                         @RequestParam(defaultValue = "1") int cantidad,
                                         @RequestParam(required = false) String mesa,
                                         @RequestParam(required = false) String tipoServicio,
                                         @AuthenticationPrincipal UserDetails user) {
        String username = user != null ? user.getUsername() : "cajero";
        Long targetId = id;
        try {
            comandaService.obtener(id);
        } catch (Exception e) {
            if (mesa != null && !mesa.isBlank()) {
                TipoServicio tipo = TipoServicio.SALA;
                try {
                    if (tipoServicio != null) tipo = TipoServicio.valueOf(tipoServicio.toUpperCase());
                } catch (Exception ignored) {}
                Comanda rec = comandaService.obtenerOCrearParaMesa(mesa, tipo, username);
                targetId = rec.getId();
            } else {
                return ResponseEntity.status(404).body("Comanda no encontrada: " + id);
            }
        }
        Comanda comanda = comandaService.anadirProducto(targetId, productoId, cantidad);
        return ResponseEntity.ok(comanda);
    }

    @PutMapping("/{id}/lineas/{lineaId}")
    public ResponseEntity<?> modificarLinea(@PathVariable Long id,
                                            @PathVariable Long lineaId,
                                            @RequestParam int cantidad) {
        Comanda comanda = comandaService.cambiarCantidadLinea(id, lineaId, cantidad);
        return ResponseEntity.ok(comanda);
    }

    @PostMapping("/{id}/lineas/{lineaId}/nota")
    public ResponseEntity<?> anadirNota(@PathVariable Long id,
                                        @PathVariable Long lineaId,
                                        @RequestParam(required = false, defaultValue = "") String nota) {
        Comanda comanda = comandaService.anadirNotaLinea(id, lineaId, nota);
        return ResponseEntity.ok(comanda);
    }

    @DeleteMapping("/{id}/lineas/{lineaId}")
    public ResponseEntity<?> eliminarLinea(@PathVariable Long id, @PathVariable Long lineaId) {
        Comanda comanda = comandaService.eliminarLinea(id, lineaId);
        return ResponseEntity.ok(comanda);
    }

    @PostMapping("/{id}/vaciar")
    public ResponseEntity<?> vaciarComanda(@PathVariable Long id) {
        Comanda comanda = comandaService.vaciarLineas(id);
        return ResponseEntity.ok(comanda);
    }

    @PostMapping("/{id}/servicio")
    public ResponseEntity<?> cambiarServicio(@PathVariable Long id,
                                             @RequestParam String tipoServicio,
                                             @RequestParam(required = false) String mesa,
                                             @RequestParam(required = false) String clienteInfo) {
        TipoServicio tipo = TipoServicio.valueOf(tipoServicio.toUpperCase());
        Comanda comanda = comandaService.actualizarEncabezado(id, tipo, mesa, clienteInfo);
        return ResponseEntity.ok(comanda);
    }

    @PostMapping("/{id}/cobrar")
    public ResponseEntity<?> cobrar(@PathVariable Long id,
                                    @RequestParam(defaultValue = "EFECTIVO") String metodoPago,
                                    @RequestParam(required = false) BigDecimal importeEntregado) {
        Comanda comanda = comandaService.cobrarYCerrar(id, metodoPago, importeEntregado);
        return ResponseEntity.ok(comanda);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        Comanda comanda = comandaService.cancelar(id);
        return ResponseEntity.ok(comanda);
    }

    /** Mueve la comanda actual a recogida (asigna primer número libre 40-80) */
    @PostMapping("/{id}/mover-recogida")
    public ResponseEntity<?> moverARecogida(@PathVariable Long id) {
        try {
            Comanda comanda = comandaService.moverARecogida(id);
            return ResponseEntity.ok(comanda);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Mueve la comanda a otra mesa libre. Devuelve 409 si la mesa destino está ocupada. */
    @PostMapping("/{id}/mover-mesa")
    public ResponseEntity<?> moverAMesa(@PathVariable Long id,
                                        @RequestParam String nuevaMesa) {
        try {
            Comanda comanda = comandaService.moverAMesa(id, nuevaMesa);
            return ResponseEntity.ok(comanda);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
