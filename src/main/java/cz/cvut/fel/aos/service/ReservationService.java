package cz.cvut.fel.aos.service;

import cz.cvut.fel.aos.data.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReservationService {
    Page<Reservation> listAllByPage(Pageable pageable);
}
