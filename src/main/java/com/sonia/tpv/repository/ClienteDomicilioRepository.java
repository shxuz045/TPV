package com.sonia.tpv.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sonia.tpv.model.ClienteDomicilio;

public interface ClienteDomicilioRepository extends JpaRepository<ClienteDomicilio, Long> {
    Optional<ClienteDomicilio> findByTelefono(String telefono);
}

