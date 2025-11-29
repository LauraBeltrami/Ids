package Repository;

import Model.GestorePiattaforma;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GestoreRepository extends JpaRepository<GestorePiattaforma, Long> {
}
