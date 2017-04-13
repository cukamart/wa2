package cz.cvut.fel.aos.service;

import cz.cvut.fel.aos.data.DestinationRepository;
import cz.cvut.fel.aos.data.entities.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Implementacia strankovania pre entitu destinacia
 */
@Service
@Transactional
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    /**
     * Ak je parameter pageable null vrati vsetky destinacie v opacnom pripade ich vyfiltruje
     * @param pageable nacita sa z url ako page & size napr .../destination/?page=0&size=3
     * @return vrati vsetky destinacie alebo stranku destinacii
     */
    @Override
    public Page<Destination> listAllByPage(Pageable pageable) {
        return destinationRepository.findAll(pageable);
    }
}
