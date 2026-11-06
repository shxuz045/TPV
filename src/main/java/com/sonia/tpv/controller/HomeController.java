package com.sonia.tpv.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sonia.tpv.model.Comanda;
import com.sonia.tpv.model.Producto;
import com.sonia.tpv.model.TipoServicio;
import com.sonia.tpv.repository.ProductoRepository;
import com.sonia.tpv.service.ComandaService;
import com.sonia.tpv.util.ProductoComparator;

@Controller
public class HomeController {

    private final ProductoRepository productoRepository;
    private final ComandaService comandaService;

    public HomeController(ProductoRepository productoRepository,
            ComandaService comandaService) {
        this.productoRepository = productoRepository;
        this.comandaService = comandaService;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails user) {
        if (user != null && user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/productos";
        }
        return "redirect:/principal";
    }

    @GetMapping("/principal")
    public String principal(Model model, @AuthenticationPrincipal UserDetails user) {
        if (user != null && user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/productos";
        }
        model.addAttribute("cajeroUsername", user != null ? user.getUsername() : "Cajero");
        return "inicio";
    }

    @GetMapping("/sala")
    public String sala(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("cajeroUsername", user != null ? user.getUsername() : "Cajero");
        comandaService.limpiarComandasVacias();
        List<Comanda> abiertas = comandaService.listarAbiertas();
        Set<String> mesasOcupadas = abiertas.stream()
                .filter(c -> c.getTipoServicio() == TipoServicio.SALA)
                .map(Comanda::getMesa)
                .filter(m -> m != null && !m.isBlank())
                .collect(Collectors.toSet());
        model.addAttribute("mesasOcupadas", mesasOcupadas);
        return "sala";
    }

    @GetMapping("/comanda-sala")
    public String comandaSala(Model model,
            @RequestParam(required = false) String mesa,
            @AuthenticationPrincipal UserDetails user) {
        return comanda(model, null, mesa, "SALA", user);
    }

    @GetMapping("/recogida")
    public String recogida(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("cajeroUsername", user != null ? user.getUsername() : "Cajero");
        comandaService.limpiarComandasVacias();
        List<Comanda> abiertas = comandaService.listarAbiertas();
        Set<String> recogidasOcupadas = abiertas.stream()
                .filter(c -> c.getTipoServicio() == TipoServicio.RECOGIDA)
                .map(Comanda::getMesa)
                .filter(m -> m != null && !m.isBlank())
                .collect(Collectors.toSet());
        model.addAttribute("recogidasOcupadas", recogidasOcupadas);
        List<Producto> productosRecogida = productoRepository.findByDisponibleTrueOrderByOrdenAsc();
        productosRecogida.sort(new ProductoComparator());
        model.addAttribute("productos", productosRecogida);
        return "recogida";
    }

    @GetMapping("/comanda")
    public String comanda(Model model,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false, defaultValue = "Mesa 1") String mesa,
            @RequestParam(defaultValue = "SALA") String tipo,
            @AuthenticationPrincipal UserDetails user) {
        String username = user != null ? user.getUsername() : "cajero";
        TipoServicio tipoServicio;
        try {
            tipoServicio = TipoServicio.valueOf(tipo.toUpperCase());
        } catch (Exception e) {
            tipoServicio = TipoServicio.SALA;
        }

        Comanda comanda;
        if (id != null) {
            comanda = comandaService.obtener(id);
            tipoServicio = comanda.getTipoServicio();
        } else {
            // Cada mesa solo puede tener una comanda abierta
            comanda = comandaService.obtenerOCrearParaMesa(mesa, tipoServicio, username);
        }

        model.addAttribute("cajeroUsername", user != null ? user.getUsername() : "Cajero");
        List<Producto> productos = productoRepository.findByDisponibleTrueOrderByOrdenAsc();
        productos.sort(new ProductoComparator());
        model.addAttribute("productos", productos);
        model.addAttribute("tipoServicio", tipoServicio.name());
        model.addAttribute("comanda", comanda);
        model.addAttribute("mesaActual", comanda.getMesa());
        return "comanda";
    }

    @GetMapping("/mesas")
    public String mesas() {
        return "redirect:/sala";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
