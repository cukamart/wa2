package cz.cvut.fel.aos.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fel.aos.data.FlightRepository;
import cz.cvut.fel.aos.data.entities.Flight;
import cz.cvut.fel.aos.errors.FlightNotCreatedException;
import cz.cvut.fel.aos.errors.FlightNotFoundException;
import cz.cvut.fel.aos.jackson.views.View;
import cz.cvut.fel.aos.model.FlightLogic;
import cz.cvut.fel.aos.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Restovy controller na manipulaciu s entitou flight.
 */
@RestController
@RequestMapping(value = "/flight")
public class FlightController {

    @Autowired
    private FlightRepository data;

    @Autowired
    private FlightService dataPagination;

    @Autowired
    private FlightLogic flightLogic;


    /**
     * Vrati lety na zaklade stranok pokial stranka nieje definovana vrati vsetky lety
     * @param pageable - nacita sa z url ako page & size napr ?page=0&size=3
     * @return vrati vsetky lety alebo ich vyfiltruje podla parametra pageable
     */
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.GET)
    @JsonView(View.FlightSummary.class)
    public Page<Flight> returnAllFlights(Pageable pageable, @RequestHeader(required = false, value = "X-Filter") String filter) throws ParseException {
        if (filter == null) {
            return dataPagination.listAllByPage(pageable);
        }

        String split[] = filter.split(" ");
        String date = split[0].substring(split[0].length() - 10);
        String date2 = split[1].substring(split[1].length() - 10);

        Page<Flight> flights = dataPagination.listAllByPage(pageable);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(date);
        Date endDate = dateFormat.parse(date2);

        List<Flight> myFLights = new ArrayList<>();

        for (Flight flight : flights) {
            Date dateOfDeparture = flight.getDateOfDeparture();
            if (dateOfDeparture.after(startDate) && dateOfDeparture.before(endDate)){
                myFLights.add(flight);
            }
        }
        return new PageImpl<Flight>(myFLights);
    }

    /**
     * Vrati konkretny let podla id. V pripade neexistujuceho letu hodi exception FlightNotFound
     * @param id - id letu
     * @return Flight - konkretny let
     */
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}", method = RequestMethod.GET)
    @JsonView(View.FlightSummary.class)
    public Flight findFlightById(@PathVariable Long id) {
        Flight flight = data.findOne(id);

        if (flight == null) {
            throw new FlightNotFoundException(id);
        }

        return flight;
    }

    /**
     * Vytvori novy let. Vrati novo vytvoreny let s url.
     * @param flight - entita let ktora sa posle cez url a nasledne vytvori
     * @param ucb - zostroji url na ktorej sa nachadza aktualne vytvorena entita
     * @return ResponseEntity<Flight> - vrati novo vytvoreny let, hlavicku obsahujucu url na novy let a httpStatus.CREATED
     */
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(View.FlightSummary.class)
    public ResponseEntity<Flight> saveFlight(@RequestBody Flight flight, UriComponentsBuilder ucb) {
        flightLogic.calculatePriceOfFlight(flight);
        Flight createdFlight;
        try {
            createdFlight = data.save(flight);
        } catch(DataIntegrityViolationException e) {
            throw new FlightNotCreatedException(flight.getName());
        }

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/flight")
                .path(String.valueOf(createdFlight.getId()))
                .build().toUri();
        headers.setLocation(locationUri);

        ResponseEntity<Flight> responseEntity = new ResponseEntity<Flight>(createdFlight, headers, HttpStatus.CREATED);

        return responseEntity;
    }

    /**
     * Vymaze let podla id. V pripade neexistujuceho id hodi exception FlightNotFound
     * @param id - letu ktory chceme vymazat
     * @return HttpStatus.NO_CONTENT (let uspesne vymazany)
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Flight> deleteFlight(@PathVariable Long id) {
        Flight flight = data.findOne(id);

        if (flight == null) {
            throw new FlightNotFoundException(id);
        }

        data.delete(id);

        return new ResponseEntity<Flight>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updatne let podla id. V pripade neexistujuceho id hodi exception FlightNotFound.
     * @param id - letu, ktory chceme updatnut.
     * @param updatedFlight - novy stav entity flight na ktory chceme updatnut.
     * @return - vrati updatnuty let a HttpStatus.OK alebo FlightNotFoundException
     */
    @JsonView(View.FlightSummary.class)
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        Flight flight = data.findOne(id);

        if (flight == null) {
            throw new FlightNotFoundException(id);
        }

        flight.setDateOfDeparture(updatedFlight.getDateOfDeparture());
        flight.setDistance(updatedFlight.getDistance());
        flight.setName(updatedFlight.getName());
        flight.setPrice(updatedFlight.getPrice());
        flight.setSeats(updatedFlight.getSeats());

        data.save(flight);

        return new ResponseEntity<>(flight, HttpStatus.OK);
    }
}

