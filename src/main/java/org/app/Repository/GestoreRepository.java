package org.app.Repository;

import org.app.Model.GestorePiattaforma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GestoreRepository extends JpaRepository<GestorePiattaforma, Long> {
    Optional<GestorePiattaforma> findByEmail(String email);
}
