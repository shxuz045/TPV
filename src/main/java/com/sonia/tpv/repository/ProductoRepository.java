package com.sonia.tpv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonia.tpv.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByDisponibleTrueOrderByOrdenAsc();
    List<Producto> findByDisponibleTrueOrderByNombreAsc();
    List<Producto> findAllByOrderByOrdenAsc();
    Optional<Producto> findByNombre(String nombre);
}
