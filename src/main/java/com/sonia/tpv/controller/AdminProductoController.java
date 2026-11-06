package com.sonia.tpv.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sonia.tpv.model.Producto;
import com.sonia.tpv.model.ProductoHistorial;
import com.sonia.tpv.model.ProductoHistorial.Accion;
import com.sonia.tpv.repository.ProductoHistorialRepository;
import com.sonia.tpv.repository.ProductoRepository;
import com.sonia.tpv.util.ProductoComparator;

@Controller
@RequestMapping("/admin")
public class AdminProductoController {

    private final ProductoRepository productoRepository;
    private final ProductoHistorialRepository historialRepository;

    public AdminProductoController(ProductoRepository productoRepository,
            ProductoHistorialRepository historialRepository) {
        this.productoRepository = productoRepository;
        this.historialRepository = historialRepository;
    }

    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("9999.99");

    /** Intercepta cualquier error inesperado de tipo o desbordamiento para que no se rompa la página */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String manejarErrorGeneral(Exception ex, RedirectAttributes flash) {
        String msg = "Se ha producido un error al procesar el producto.";
        String exMsg = (ex.getMessage() != null) ? ex.getMessage().toLowerCase() : "";
        if (ex instanceof org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
                || ex instanceof IllegalArgumentException
                || exMsg.contains("numeric")
                || exMsg.contains("overflow")
                || exMsg.contains("out of range")) {
            msg = "El precio es demasiado alto (máximo 9.999,99 €) o no tiene un formato válido.";
        }
        flash.addFlashAttribute("error", msg);
        return "redirect:/admin/productos";
    }

    /** Lista todos los productos ordenados por sección y número de menor a mayor */
    @GetMapping("/productos")
    public String listar(Model model) {
        List<Producto> productos = productoRepository.findAll();
        productos.sort(new ProductoComparator());
        model.addAttribute("productos", productos);

        // Lista unificada de categorías: primero las oficiales, luego las nuevas creadas
        List<String> categoriasExistentes = new java.util.ArrayList<>(ProductoComparator.CATEGORIAS_ORDEN);
        for (Producto p : productos) {
            String cat = p.getCategoria();
            if (cat != null && !cat.isBlank()) {
                boolean yaExiste = categoriasExistentes.stream()
                        .anyMatch(c -> c.equalsIgnoreCase(cat.trim()));
                if (!yaExiste) {
                    categoriasExistentes.add(cat.trim());
                }
            }
        }
        model.addAttribute("categoriasExistentes", categoriasExistentes);

        return "admin/productos";
    }

    /** Historial de eliminaciones (accesible desde el perfil) */
    @GetMapping("/historial/eliminaciones")
    public String historialEliminaciones(Model model) {
        model.addAttribute("historialEliminaciones",
                historialRepository.findByAccionOrderByFechaDesc(Accion.ELIMINAR));
        return "admin/historial-eliminaciones";
    }

    /** Historial de cambios (accesible desde el perfil) */
    @GetMapping("/historial/cambios")
    public String historialCambios(Model model) {
        model.addAttribute("historialCambios",
                historialRepository.findByAccionInOrderByFechaDesc(List.of(Accion.CREAR, Accion.MODIFICAR)));
        return "admin/historial-cambios";
    }

    /** Redirigir /admin/historial a eliminaciones */
    @GetMapping("/historial")
    public String historial() {
        return "redirect:/admin/historial/eliminaciones";
    }

    /** Crea un producto nuevo */
    @PostMapping("/productos/crear")
    public String crear(@RequestParam(required = false) String numero,
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam(defaultValue = "GENERAL") String categoria,
            RedirectAttributes flash) {
        numero = (numero != null) ? numero.trim() : "";
        if (numero.endsWith(".")) {
            numero = numero.substring(0, numero.length() - 1).trim();
        }
        nombre = nombre.trim();
        categoria = categoria.trim();

        String errorTexto = validarTextoPlato(nombre);
        if (errorTexto != null) {
            flash.addFlashAttribute("error", errorTexto);
            return "redirect:/admin/productos";
        }

        String nombreFinal = (!numero.isEmpty()) ? (numero + ". " + nombre) : nombre;

        String errorCodigo = validarNumeroPlato(!numero.isEmpty() ? numero : nombreFinal, null);
        if (errorCodigo != null) {
            flash.addFlashAttribute("error", errorCodigo);
            return "redirect:/admin/productos";
        }

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0 || precio.compareTo(PRECIO_MAXIMO) > 0) {
            flash.addFlashAttribute("error", "El precio es demasiado alto o inválido (máximo 9.999,99 €).");
            return "redirect:/admin/productos";
        }

        try {
            int orden = calcularOrden(categoria, nombreFinal);
            Producto p = new Producto(nombreFinal, precio, categoria, true, orden);
            productoRepository.save(p);

            // Registrar en historial
            historialRepository.save(new ProductoHistorial(p.getId(), Accion.CREAR, nombreFinal, precio, categoria));

            flash.addFlashAttribute("ok", "Producto \"" + nombreFinal + "\" creado correctamente en \"" + categoria + "\".");
        } catch (Exception ex) {
            flash.addFlashAttribute("error", "No se pudo crear el producto: datos o precio no válidos.");
        }

        return "redirect:/admin/productos";
    }

    /** Edita nombre, precio y categoría de un producto */
    @PostMapping("/productos/{id}/editar")
    public String editar(@PathVariable Long id,
            @RequestParam(required = false) String numero,
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam String categoria,
            RedirectAttributes flash) {
        Producto p = productoRepository.findById(id).orElse(null);
        if (p == null) {
            flash.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/admin/productos";
        }

        numero = (numero != null) ? numero.trim() : "";
        if (numero.endsWith(".")) {
            numero = numero.substring(0, numero.length() - 1).trim();
        }
        nombre = nombre.trim();
        categoria = categoria.trim();

        String errorTexto = validarTextoPlato(nombre);
        if (errorTexto != null) {
            flash.addFlashAttribute("error", errorTexto);
            return "redirect:/admin/productos";
        }

        String nombreFinal = (!numero.isEmpty()) ? (numero + ". " + nombre) : nombre;

        String errorCodigo = validarNumeroPlato(!numero.isEmpty() ? numero : nombreFinal, id);
        if (errorCodigo != null) {
            flash.addFlashAttribute("error", errorCodigo);
            return "redirect:/admin/productos";
        }

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0 || precio.compareTo(PRECIO_MAXIMO) > 0) {
            flash.addFlashAttribute("error", "El precio es demasiado alto o inválido (máximo 9.999,99 €).");
            return "redirect:/admin/productos";
        }

        try {
            // Guardar valores ANTERIORES en el historial antes de modificar
            historialRepository.save(new ProductoHistorial(p.getId(), Accion.MODIFICAR,
                    p.getNombre(), p.getPrecio(), p.getCategoria()));

            p.setNombre(nombreFinal);
            p.setPrecio(precio);
            p.setCategoria(categoria);
            p.setOrden(calcularOrden(categoria, nombreFinal));
            productoRepository.save(p);
            flash.addFlashAttribute("ok", "Producto actualizado correctamente a \"" + nombreFinal + "\".");
        } catch (Exception ex) {
            flash.addFlashAttribute("error", "No se pudo actualizar el producto: el precio es demasiado alto.");
        }

        return "redirect:/admin/productos";
    }

    /** Elimina un producto definitivamente */
    @PostMapping("/productos/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        Producto p = productoRepository.findById(id).orElse(null);
        if (p == null) {
            flash.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/admin/productos";
        }

        // Guardar en historial antes de borrar
        historialRepository.save(new ProductoHistorial(p.getId(), Accion.ELIMINAR,
                p.getNombre(), p.getPrecio(), p.getCategoria()));

        productoRepository.delete(p);
        flash.addFlashAttribute("ok", "\"" + p.getNombre() + "\" eliminado.");
        return "redirect:/admin/productos";
    }

    /** Borra todo el historial de eliminaciones */
    @PostMapping("/historial/eliminaciones/borrar-todo")
    @org.springframework.transaction.annotation.Transactional
    public String borrarTodoHistorialEliminaciones(Model model) {
        historialRepository.deleteAllByAccion(Accion.ELIMINAR);
        historialRepository.flush();
        model.addAttribute("ok", "Historial de eliminaciones borrado correctamente.");
        model.addAttribute("historialEliminaciones",
                historialRepository.findByAccionOrderByFechaDesc(Accion.ELIMINAR));
        return "admin/historial-eliminaciones";
    }

    /** Borra todo el historial de cambios (creaciones y modificaciones) */
    @PostMapping("/historial/cambios/borrar-todo")
    @org.springframework.transaction.annotation.Transactional
    public String borrarTodoHistorialCambios(Model model) {
        historialRepository.deleteAllByAccionIn(java.util.List.of(Accion.CREAR, Accion.MODIFICAR));
        historialRepository.flush();
        model.addAttribute("ok", "Historial de cambios borrado correctamente.");
        model.addAttribute("historialCambios",
                historialRepository.findByAccionInOrderByFechaDesc(java.util.List.of(Accion.CREAR, Accion.MODIFICAR)));
        return "admin/historial-cambios";
    }

    /** Revertir un cambio del historial */
    @PostMapping({"/historial/{id}/revertir", "/productos/historial/{id}/revertir"})
    public String revertir(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "eliminaciones") String origen,
            RedirectAttributes flash) {
        ProductoHistorial h = historialRepository.findById(id).orElse(null);
        if (h == null) {
            flash.addFlashAttribute("error", "Entrada de historial no encontrada.");
            return "redirect:/admin/historial/" + origen;
        }

        switch (h.getAccion()) {
            case CREAR:
                // Revertir creación = eliminar el producto
                Producto creado = (h.getProductoId() != null ? productoRepository.findById(h.getProductoId()).orElse(null) : null);
                if (creado == null) {
                    creado = productoRepository.findByNombre(h.getNombre()).orElse(null);
                }
                if (creado != null) {
                    productoRepository.delete(creado);
                    flash.addFlashAttribute("ok", "Creación revertida: \"" + h.getNombre() + "\" eliminado.");
                } else {
                    flash.addFlashAttribute("error", "El producto ya no existe.");
                }
                break;

            case MODIFICAR:
                // Revertir modificación = restaurar valores anteriores
                Producto modificado = (h.getProductoId() != null ? productoRepository.findById(h.getProductoId()).orElse(null) : null);
                if (modificado == null) {
                    modificado = productoRepository.findByNombre(h.getNombre()).orElse(null);
                }
                if (modificado != null) {
                    modificado.setNombre(h.getNombre());
                    modificado.setPrecio(h.getPrecio());
                    modificado.setCategoria(h.getCategoria());
                    modificado.setOrden(calcularOrden(h.getCategoria(), h.getNombre()));
                    productoRepository.save(modificado);
                    flash.addFlashAttribute("ok", "Modificación revertida: restaurado a \"" + h.getNombre() + "\".");
                } else {
                    flash.addFlashAttribute("error", "El producto no se encontró para restaurar.");
                }
                break;

            case ELIMINAR:
                // Revertir eliminación = re-crear el producto
                int orden = calcularOrden(h.getCategoria(), h.getNombre());
                Producto restaurado = new Producto(h.getNombre(), h.getPrecio(), h.getCategoria(), true, orden);
                productoRepository.save(restaurado);
                flash.addFlashAttribute("ok", "Eliminación revertida: \"" + h.getNombre() + "\" restaurado en la carta.");
                break;
        }

        // Borrar la entrada del historial ya usada
        historialRepository.delete(h);

        return "redirect:/admin/historial/" + origen;
    }

    private int calcularOrden(String categoria, String nombre) {
        int catRank = ProductoComparator.getCategoryRank(categoria);
        int num = 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(nombre);
        if (m.find()) {
            try {
                num = Integer.parseInt(m.group());
            } catch (Exception ignored) {}
        }
        return (catRank * 1000) + num;
    }

    /**
     * Valida que el plato tenga obligatoriamente un número (puede incluir letras, ej: 8, 8b, 8c, P1)
     * y que no esté ya en uso por otro plato existente.
     */
    private String validarNumeroPlato(String nombre, Long idIgnorar) {
        String codigoNuevo = ProductoComparator.extraerCodigo(nombre);
        if (codigoNuevo == null || codigoNuevo.isBlank()) {
            return "El plato debe tener obligatoriamente un número o código identificador (ejemplos: 8, 8b, 8c, P1).";
        }

        List<Producto> todos = productoRepository.findAll();
        for (Producto existente : todos) {
            if (idIgnorar != null && existente.getId().equals(idIgnorar)) {
                continue; // Mismo producto al editar
            }
            String codExistente = ProductoComparator.extraerCodigo(existente.getNombre());
            if (codExistente != null && codExistente.equalsIgnoreCase(codigoNuevo)) {
                return "Ya existe un plato con el número/código \"" + codigoNuevo.toUpperCase()
                        + "\" (\"" + existente.getNombre() + "\"). Los números de plato no se pueden repetir.";
            }
        }

        return null;
    }

    /**
     * Valida que el nombre del plato tenga al menos 2 letras y no contenga puntos ni comas.
     */
    private String validarTextoPlato(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "El nombre del plato no puede estar vacío.";
        }
        if (nombre.contains(".") || nombre.contains(",")) {
            return "El nombre del plato no puede contener puntos ni comas.";
        }
        long letras = nombre.codePoints().filter(Character::isLetter).count();
        if (letras < 2) {
            return "El nombre del plato debe tener al menos 2 letras.";
        }
        return null;
    }
}
