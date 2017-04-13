package cz.cvut.fel.aos.data;

import cz.cvut.fel.aos.data.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vygeneruje CRUD operacie pre entitu Reservation.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
