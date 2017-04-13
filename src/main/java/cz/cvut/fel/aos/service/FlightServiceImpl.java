package cz.cvut.fel.aos.service;

import cz.cvut.fel.aos.data.FlightRepository;
import cz.cvut.fel.aos.data.entities.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Implementacia strankovania pre entitu Flight
 */
@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository data;

    /**
     * Ak je parameter pageable null vrati vsetky lety v opacnom pripade ich vyfiltruje
     * @param pageable nacita sa z url ako page & size napr .../flight/?page=0&size=3
     * @return vrati vsetky lety alebo stranku destinacii
     */
    @Override
    public Page<Flight> listAllByPage(Pageable pageable) {
        return data.findAll(pageable);
    }
}
