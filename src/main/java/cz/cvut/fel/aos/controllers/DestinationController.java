package cz.cvut.fel.aos.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fel.aos.data.DestinationRepository;
import cz.cvut.fel.aos.data.entities.Destination;
import cz.cvut.fel.aos.errors.DestinationNotCreatedException;
import cz.cvut.fel.aos.errors.DestinationNotFoundException;
import cz.cvut.fel.aos.jackson.views.View;
import cz.cvut.fel.aos.model.DestinationLogic;
import cz.cvut.fel.aos.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Restovy controller na manipulaciu s entitou destination.
 */
@RestController
@RequestMapping(value = "/destination")
public class DestinationController {

    @Autowired
    private DestinationRepository data;

    @Autowired
    private DestinationService dataPagination;

    @Autowired
    private DestinationLogic destinationLogic;

    /**
     * Vrati destinacie na zaklade stranok pokial stranka nieje definovana vrati vsetky destinacie
     * @param pageable - nacita sa z url ako page & size napr ?page=0&size=3
     * @return vrati vsetky destinacie alebo stranku destinacii
     */
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.GET)
    @JsonView(View.DestinationSummary.class)
    public Page<Destination> returnDestinationsByPage(Pageable pageable){
        return dataPagination.listAllByPage(pageable);
    }


    /**
     * Vrati konkretnu destinaciu podla id. V pripade neexistujucej rezervacie hodi exception DestinationNotFound
     *
     * @param id - id destinacie
     * @return Destination - konkretna destinacia
     */
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}", method = RequestMethod.GET)
    @JsonView(View.DestinationSummary.class)
    public Destination findDestinationById(@PathVariable Long id) {
        Destination destination = data.findOne(id);

        if (destination == null) {
            throw new DestinationNotFoundException(id);
        }

        return destination;
    }

    /**
     * Vytvori novu destinaciu. Vrati novo vytvorenu destinaciu s url.
     *
     * @param destination - entita destination ktora sa posle cez url a nasledne vytvori
     * @param ucb         - zostroji url na ktorej sa nachadza aktualne vytvorena entita
     * @return ResponseEntity<Destination> - vrati novo vytvorenu destinaciu, hlavicku obsahujucu url na novu destinaciu a httpStatus.CREATED
     */
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(View.DestinationSummary.class)
    public ResponseEntity<Destination> saveDestination(@RequestBody Destination destination, UriComponentsBuilder ucb) {

        destination = destinationLogic.addLocationToDestination(destination);

        Destination createdDestination;
        try {
            createdDestination = data.save(destination);
        } catch (DataIntegrityViolationException e) {
            throw new DestinationNotCreatedException(destination.getName());
        }

        HttpHeaders headers = new HttpHeaders();

        URI locationUri = ucb.path("/destination")
                .path(String.valueOf(createdDestination.getId()))
                .build().toUri();

        headers.setLocation(locationUri);

        ResponseEntity<Destination> responseEntity = new ResponseEntity<Destination>(createdDestination, headers, HttpStatus.CREATED);

        return responseEntity;
    }

    /**
     * Vymaze destinaciu podla id. V pripade neexistujuceho id hodi exception DestinationNotFound
     *
     * @param id - destinacie ktoru chceme vymazat
     * @return HttpStatus.NO_CONTENT (destinacia uspesne vymazana)
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Destination> deleteDestination(@PathVariable Long id) {
        Destination destination = data.findOne(id);

        if (destination == null) {
            throw new DestinationNotFoundException(id);
        }

        data.delete(id);

        return new ResponseEntity<Destination>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updatne destinaciu podla id. V pripade neexistujuceho id hodi exception DestinationNotFound.
     *
     * @param id                 - destinacie, ktoru chceme updatnut.
     * @param updatedDestination - novy stav entity destination na ktory chceme updatnut.
     * @return - vrati updatnutu destinaciu a HttpStatus.OK alebo DestinationNotFoundException
     */
    @JsonView(View.DestinationSummary.class)
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Destination> updateFlight(@PathVariable Long id, @RequestBody Destination updatedDestination) {
        Destination destination = data.findOne(id);

        if (destination == null) {
            throw new DestinationNotFoundException(id);
        }

        destination.setLat(updatedDestination.getLat());
        destination.setLon(updatedDestination.getLon());
        destination.setName(updatedDestination.getName());

        data.save(destination);

        return new ResponseEntity<>(destination, HttpStatus.OK);
    }
}
