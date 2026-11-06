package com.sonia.tpv.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sonia.tpv.model.Comanda;
import com.sonia.tpv.model.EstadoComanda;
import com.sonia.tpv.model.FaseDomicilio;
import com.sonia.tpv.model.OrigenPedido;
import com.sonia.tpv.service.ComandaService;

@Controller
@RequestMapping("/domicilio")
public class DomicilioController {

    private final ComandaService comandaService;

    public DomicilioController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("cajeroUsername", user != null ? user.getUsername() : "Cajero");

        List<Comanda> todos = comandaService.listarDomicilios();

        // Pedidos a domicilio abiertos actualmente
        List<Comanda> activos = todos.stream()
                .filter(c -> c.getEstado() == EstadoComanda.ABIERTA)
                .toList();

        // Historial reciente de pedidos entregados/cobrados
        List<Comanda> finalizados = todos.stream()
                .filter(c -> c.getEstado() == EstadoComanda.CERRADA)
                .limit(15)
                .toList();

        model.addAttribute("pedidosActivos", activos);
        model.addAttribute("pedidosFinalizados", finalizados);

        return "domicilio";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam String nombreCliente,
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

        Comanda comanda = comandaService.crearDomicilio(
                nombreCliente,
                telefono,
                direccion,
                notasEntrega,
                horaFinal,
                username);

        flash.addFlashAttribute("mensajeOk",
                "Pedido " + comanda.getMesa() + " registrado. Añade los platos a continuación.");
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
