package com.sonia.tpv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonia.tpv.model.Comanda;
import com.sonia.tpv.model.EstadoComanda;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    List<Comanda> findByEstadoOrderByFechaCreacionDesc(EstadoComanda estado);
    List<Comanda> findAllByOrderByFechaCreacionDesc();
    Optional<Comanda> findFirstByMesaAndEstado(String mesa, EstadoComanda estado);
    List<Comanda> findByMesaAndEstado(String mesa, EstadoComanda estado);
    List<Comanda> findByTipoServicioOrderByFechaCreacionDesc(com.sonia.tpv.model.TipoServicio tipoServicio);
}
