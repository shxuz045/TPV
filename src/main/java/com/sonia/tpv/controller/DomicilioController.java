package com.sonia.tpv.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sonia.tpv.model.Comanda;
import com.sonia.tpv.model.EstadoComanda;
import com.sonia.tpv.model.FaseDomicilio;
import com.sonia.tpv.model.OrigenPedido;
import com.sonia.tpv.service.ClienteDomicilioService;
import com.sonia.tpv.service.ComandaService;

@Controller
@RequestMapping("/domicilio")
public class DomicilioController {

    private final ComandaService comandaService;
    private final ClienteDomicilioService clienteService;

    public DomicilioController(ComandaService comandaService, ClienteDomicilioService clienteService) {
        this.comandaService = comandaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("cajeroUsername", user != null ? user.getUsername() : "Cajero");
        List<Comanda> todos = comandaService.listarDomicilios();
        List<Comanda> activos = todos.stream().filter(c -> c.getEstado() == EstadoComanda.ABIERTA).toList();
        List<Comanda> finalizados = todos.stream().filter(c -> c.getEstado() == EstadoComanda.CERRADA).limit(15)
                .toList();
        model.addAttribute("pedidosActivos", activos);
        model.addAttribute("pedidosFinalizados", finalizados);
        return "domicilio";
    }

    /** API: buscar cliente por teléfono — devuelve nombre, lista de direcciones */
    @GetMapping("/verificar-telefono")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarTelefono(@RequestParam("telefono") String telefono) {
        Map<String, Object> info = clienteService.buscarPorTelefono(telefono);
        return ResponseEntity.ok(info);
    }

    /** API: eliminar una dirección guardada de un cliente */
    @PostMapping("/cliente/eliminar-direccion")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarDireccion(
            @RequestParam String telefono,
            @RequestParam String direccion) {
        Map<String, Object> resultado = clienteService.eliminarDireccion(telefono, direccion);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/crear")
    public String crear(
            @RequestParam(required = false, defaultValue = "") String nombreCliente,
            @RequestParam(required = false, defaultValue = "") String telefono,
            @RequestParam String direccion,
            @RequestParam(required = false, defaultValue = "") String notasEntrega,
            @RequestParam(defaultValue = "momento") String tipoHorario,
            @RequestParam(required = false) String horaProgramada,
            @AuthenticationPrincipal UserDetails user,
            RedirectAttributes flash) {

        String username = user != null ? user.getUsername() : "cajero";
        String horaFinal = "Al momento";
        if ("programado".equalsIgnoreCase(tipoHorario) && horaProgramada != null && !horaProgramada.isBlank()) {
            horaFinal = horaProgramada.trim();
        }

        String telefonoLimpio = clienteService.limpiarTelefono(telefono);

        // Guardar/actualizar cliente en la tabla de clientes
        if (!telefonoLimpio.isBlank()) {
            clienteService.guardarOActualizar(telefonoLimpio, nombreCliente, direccion);
        }

        Comanda comanda = comandaService.crearDomicilio(nombreCliente, telefonoLimpio, direccion, notasEntrega, horaFinal, username);
        flash.addFlashAttribute("mensajeOk", "Pedido " + comanda.getMesa() + " registrado. Añade los platos a continuación.");

        return "redirect:/comanda?id=" + comanda.getId() + "&tipo=DOMICILIO";
    }

    @PostMapping("/{id}/fase")
    public String cambiarFase(@PathVariable Long id,
            @RequestParam String fase,
            @RequestParam(required = false) String repartidor,
            RedirectAttributes flash) {
        try {
            FaseDomicilio nuevaFase = FaseDomicilio.valueOf(fase.toUpperCase());
            comandaService.cambiarFaseDomicilio(id, nuevaFase, repartidor);
            flash.addFlashAttribute("mensajeOk", "Pedido actualizado a: " + nuevaFase.getEtiqueta());
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al cambiar de fase: " + e.getMessage());
        }
        return "redirect:/domicilio";
    }

    @PostMapping("/{id}/cobrar")
    public String cobrar(@PathVariable Long id,
            @RequestParam(defaultValue = "EFECTIVO") String metodoPago,
            @RequestParam(required = false) BigDecimal importeEntregado,
            RedirectAttributes flash) {
        try {
            comandaService.cobrarYCerrar(id, metodoPago, importeEntregado);
            comandaService.cambiarFaseDomicilio(id, FaseDomicilio.ENTREGADO, null);
            flash.addFlashAttribute("mensajeOk", "Pedido cobrado y marcado como entregado.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al cobrar: " + e.getMessage());
        }
        return "redirect:/domicilio";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            Comanda c = comandaService.obtener(id);
            String mesa = (c != null && c.getMesa() != null) ? c.getMesa() : ("Pedido #" + id);
            comandaService.cancelar(id);
            flash.addFlashAttribute("pedidoCanceladoMesa", mesa);
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al cancelar: " + e.getMessage());
        }
        return "redirect:/domicilio";
    }
}
