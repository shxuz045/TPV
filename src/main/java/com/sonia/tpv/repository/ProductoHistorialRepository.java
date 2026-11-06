package com.sonia.tpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sonia.tpv.model.ProductoHistorial;
import com.sonia.tpv.model.ProductoHistorial.Accion;

public interface ProductoHistorialRepository extends JpaRepository<ProductoHistorial, Long> {

    /** Todos ordenados por fecha descendente */
    List<ProductoHistorial> findAllByOrderByFechaDesc();

    /** Historial filtrado por tipo de acción, del más reciente al más antiguo */
    List<ProductoHistorial> findByAccionOrderByFechaDesc(Accion accion);

    /** Historial de cambios (CREAR + MODIFICAR) */
    List<ProductoHistorial> findByAccionInOrderByFechaDesc(List<Accion> acciones);

    /** Borra todas las entradas ELIMINAR */
    @Transactional
    @Modifying
    @Query("DELETE FROM ProductoHistorial h WHERE h.accion = :accion")
    void deleteAllByAccion(Accion accion);

    /** Borra todas las entradas CREAR + MODIFICAR */
    @Transactional
    @Modifying
    @Query("DELETE FROM ProductoHistorial h WHERE h.accion IN :acciones")
    void deleteAllByAccionIn(List<Accion> acciones);
}
