package cz.cvut.fel.aos.service;

import cz.cvut.fel.aos.data.ReservationRepository;
import cz.cvut.fel.aos.data.entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Implementacia strankovania pre entitu Reservation
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Ak je parameter pageable null vrati vsetky rezervacie v opacnom pripade ich vyfiltruje
     * @param pageable nacita sa z url ako page & size napr .../reservation/?page=0&size=3
     * @return vrati vsetky rezervacie alebo stranku destinacii
     */
    @Override
    public Page<Reservation> listAllByPage(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
}
