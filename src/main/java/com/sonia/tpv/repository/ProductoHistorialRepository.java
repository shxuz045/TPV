package com.sonia.tpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonia.tpv.model.ProductoHistorial;
import com.sonia.tpv.model.ProductoHistorial.Accion;

public interface ProductoHistorialRepository extends JpaRepository<ProductoHistorial, Long> {

    /** Todos ordenados por fecha descendente */
    List<ProductoHistorial> findAllByOrderByFechaDesc();

    /** Historial filtrado por tipo de acción, del más reciente al más antiguo */
    List<ProductoHistorial> findByAccionOrderByFechaDesc(Accion accion);

    /** Historial de cambios (CREAR + MODIFICAR) */
    List<ProductoHistorial> findByAccionInOrderByFechaDesc(List<Accion> acciones);
}

