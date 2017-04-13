package cz.cvut.fel.aos.service;

import cz.cvut.fel.aos.data.entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Martin on 20.11.2016.
 */
public interface FlightService {
    Page<Flight> listAllByPage(Pageable pageable);
}
