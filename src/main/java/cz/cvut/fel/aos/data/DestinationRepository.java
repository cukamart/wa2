package cz.cvut.fel.aos.data;


import cz.cvut.fel.aos.data.entities.Destination;
import cz.cvut.fel.aos.service.DestinationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vygeneruje CRUD operacie pre entitu Destination.
 */
public interface DestinationRepository extends JpaRepository<Destination, Long> {

}
