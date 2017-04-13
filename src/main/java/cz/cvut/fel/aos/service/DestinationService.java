package cz.cvut.fel.aos.service;

import cz.cvut.fel.aos.data.entities.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DestinationService {
    Page<Destination> listAllByPage(Pageable pageable);
}
