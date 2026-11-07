package com.sonia.tpv.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonia.tpv.model.ClienteDomicilio;
import com.sonia.tpv.repository.ClienteDomicilioRepository;

@Service
public class ClienteDomicilioService {

    private final ClienteDomicilioRepository repo;

    public ClienteDomicilioService(ClienteDomicilioRepository repo) {
        this.repo = repo;
    }

    /** Limpia el teléfono dejando sólo dígitos (máximo 9 números positivos) */
    public String limpiarTelefono(String telefono) {
        if (telefono == null) return "";
        String digitos = telefono.replaceAll("[^0-9]", "");
        if (digitos.length() > 9) {
            digitos = digitos.substring(0, 9);
        }
        return digitos;
    }

    /** Busca un cliente por su teléfono y devuelve un mapa con sus datos para la API */
    public Map<String, Object> buscarPorTelefono(String telefono) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        String telLimpio = limpiarTelefono(telefono);
        if (telLimpio.length() < 6) {
            resultado.put("encontrado", false);
            return resultado;
        }

        Optional<ClienteDomicilio> opt = repo.findByTelefono(telLimpio);
        if (opt.isEmpty()) {
            resultado.put("encontrado", false);
            return resultado;
        }

        ClienteDomicilio c = opt.get();
        resultado.put("encontrado", true);
        resultado.put("id", c.getId());
        resultado.put("nombre", c.getNombre() != null ? c.getNombre() : "");
        resultado.put("telefono", c.getTelefono());
        resultado.put("direcciones", c.getDirecciones());
        // Para compatibilidad con el flujo actual: primera dirección como "direccion"
        resultado.put("direccion", c.getDirecciones().isEmpty() ? "" : c.getDirecciones().get(0));
        return resultado;
    }

    /** Guarda o actualiza el cliente. Añade la dirección si no estaba ya. */
    @Transactional
    public ClienteDomicilio guardarOActualizar(String telefono, String nombre, String direccion) {
        String telLimpio = limpiarTelefono(telefono);
        if (telLimpio.isBlank()) return null;

        ClienteDomicilio cliente = repo.findByTelefono(telLimpio)
                .orElseGet(() -> {
                    ClienteDomicilio c = new ClienteDomicilio();
                    c.setTelefono(telLimpio);
                    c.setFechaCreacion(LocalDateTime.now());
                    return c;
                });

        if (nombre != null && !nombre.isBlank()) {
            cliente.setNombre(nombre.trim());
        }

        if (direccion != null && !direccion.isBlank()) {
            String dirTrim = direccion.trim();
            List<String> dirs = new ArrayList<>(cliente.getDirecciones());
            // Poner la dirección usada en primer lugar (más reciente)
            dirs.remove(dirTrim);
            dirs.add(0, dirTrim);
            // Máximo 5 direcciones guardadas
            if (dirs.size() > 5) dirs = dirs.subList(0, 5);
            cliente.setDirecciones(dirs);
        }

        cliente.setUltimaVisita(LocalDateTime.now());
        return repo.save(cliente);
    }

    /** Elimina una dirección concreta de un cliente */
    @Transactional
    public Map<String, Object> eliminarDireccion(String telefono, String direccion) {
        String telLimpio = limpiarTelefono(telefono);
        Map<String, Object> resultado = new LinkedHashMap<>();
        Optional<ClienteDomicilio> opt = repo.findByTelefono(telLimpio);
        if (opt.isEmpty()) {
            resultado.put("ok", false);
            return resultado;
        }
        ClienteDomicilio c = opt.get();
        c.getDirecciones().remove(direccion.trim());
        repo.save(c);
        resultado.put("ok", true);
        resultado.put("direcciones", c.getDirecciones());
        return resultado;
    }
}

