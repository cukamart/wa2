package cz.cvut.fel.aos.data;

import cz.cvut.fel.aos.data.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vygeneruje CRUD operacie pred entitu Flight.
 */
public interface FlightRepository extends JpaRepository<Flight, Long> {

}
