package cz.cvut.fel.aos.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fel.aos.data.FlightRepository;
import cz.cvut.fel.aos.data.ReservationRepository;
import cz.cvut.fel.aos.data.entities.Reservation;
import cz.cvut.fel.aos.data.entities.State;
import cz.cvut.fel.aos.errors.*;
import cz.cvut.fel.aos.jackson.views.View;
import cz.cvut.fel.aos.model.ReservationLogic;
import cz.cvut.fel.aos.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Restovy controller na manipulaciu s entitou reservation.
 */
@RestController
@RequestMapping(value = "/reservation")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationData;

    @Autowired
    private FlightRepository flightData;

    @Autowired
    private ReservationService dataPagination;

    @Autowired
    private ReservationLogic checkReservation;


    /**
     * Vrati rezervacie na zaklade stranok pokial stranka nieje definovana vrati vsetky rezervacie
     * @param pageable - nacita sa z url ako page & size napr ?page=0&size=3
     * @return vrati vsetky rezervacie alebo ich vyfiltruje podla parametra pageable
     */
    @JsonView(View.ReservationSummary.class)
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.GET)
    public Page<Reservation> returnAllReservations(Pageable pageable) {
        return dataPagination.listAllByPage(pageable);
    }

    /**
     * Vrati konkretnu rezervaciu podla id. V pripade neexistujucej rezervacie hodi exception ReservationNotFound
     * @param id - id rezervacie
     * @return Reservation - konkretna rezervacia
     */
    @JsonView(View.ReservationSummary.class)
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}", method = RequestMethod.GET)
    public Reservation findReservationById(@PathVariable Long id, @RequestHeader(required = false, value = "X-Password") String password) {
        Reservation reservation = reservationData.findOne(id);

        if (reservation == null) {
            throw new ReservationNotFoundException(id);
        }

        if (!isAdmin()) {
            if (password == null || !password.equals(reservation.getPassword())) {
                throw new ReservationAccessDeniedException();
            }
        }

        return reservation;
    }

    private boolean isAdmin() {
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority role : roles) {
            if (role.toString().equals("ROLE_ADMIN")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vytvori novu rezervaciu. Vrati password novo vytvorenu rezervaciu s url.
     * Kontroluje ci si zakaznik nekupuje listok na uz plny let - FlightIsFullException.
     * Taktiez kontroluje ci si kupuje listok na existujuci let - FlightNotFoundException.
     * @param newReservation - entita reservation ktora sa posle cez url a nasledne vytvori
     * @param ucb - zostroji url na ktorej sa nachadza aktualne vytvorena entita
     * @return ResponseEntity<String> - vrati password k vytvorenej rezervacii, hlavicku obsahujucu url na novu rezervaciu a httpStatus.CREATED
     */
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveReservation(@RequestBody Reservation newReservation, UriComponentsBuilder ucb) {

        Integer maxSeats = checkReservation.getNumberOfSeats(newReservation, flightData.findAll());

        if (maxSeats == null) {
            throw new FlightNotFoundException(newReservation.getFlight().getId());
        }

        if (checkReservation.isFlightFull(reservationData.findAll(), newReservation, maxSeats)) {
            int reservedSeats = this.checkReservation.getReservedSeats(reservationData.findAll(), newReservation);

            throw new FlightIsFullException(reservedSeats, maxSeats);
        }

        Reservation createdReservation = reservationData.save(newReservation);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/reservation")
                .path(String.valueOf(createdReservation.getId()))
                .build().toUri();
        headers.setLocation(locationUri);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(createdReservation.getPassword(), headers, HttpStatus.CREATED);

        return responseEntity;
    }

    @JsonView(View.ReservationSummary.class)
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}/{creditCard}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Reservation> payReservation(@PathVariable Long id, @PathVariable Long creditCard) {
        Reservation reservation = reservationData.findOne(id);

        if (reservation == null) {
            throw new ReservationNotFoundException(id);
        }

        if (reservation.getState().equals(State.NEW)) {
            reservation.setState(State.PAID);
            reservationData.save(reservation);

            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            throw new ReservationCannotBePaidException(id);
        }

    }

    @JsonView(View.ReservationSummary.class)
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}/{creditCard}/ok", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Reservation> payReservationTest(@PathVariable Long id, @PathVariable Long creditCard) {
        Reservation reservation = reservationData.findOne(id);

        if (reservation == null) {
            throw new ReservationNotFoundException(id);
        }

        if (reservation.getState().equals(State.NEW)) {
            reservation.setState(State.PAID);
            reservationData.save(reservation);

            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            throw new ReservationCannotBePaidException(id);
        }

    }

    /**
     * Vymaze rezervaciu podla id. V pripade neexistujuceho id hodi exception ReservationNotFound
     * @param id - rezervacie ktoru chceme vymazat
     * @return HttpStatus.NO_CONTENT (rezervacia uspesne vymazana)
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Reservation> deleteReservation(@PathVariable Long id) {
        Reservation reservation = reservationData.findOne(id);

        if (reservation == null) {
            throw new ReservationNotFoundException(id);
        }

        if (reservation.getState().equals(State.PAID)) {
            throw new ReservationNotDeletableException(id);
        }

        reservationData.delete(id);

        return new ResponseEntity<Reservation>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updatne rezeraciu podla id. V pripade neexistujuceho id hodi exception ReservationNotFound.
     * @param id - rezervacie, ktoru chceme updatnut.
     * @param updatedReservation - novy stav entity reservation na ktory chceme updatnut.
     * @return - vrati updatnutu rezervaciu a HttpStatus.OK alebo ReservationNotFoundException
     */
    @JsonView(View.ReservationSummary.class)
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        Reservation reservation = reservationData.findOne(id);

        if (reservation == null) {
            throw new ReservationNotFoundException(id);
        }

        reservation.setSeats(updatedReservation.getSeats());
        reservation.setFlight(updatedReservation.getFlight());
        reservation.setState(updatedReservation.getState());

        reservationData.save(reservation);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }


    /**
     * Vytlaci listok (vrati .txt subor)
     * @param id - id listka
     * @return bytearray vo forme textoveho suboru
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> printTicket(@PathVariable Long id) throws IllegalAccessException {
        Reservation reservation = reservationData.findOne(id);

        if (reservation == null) {
            throw new ReservationNotFoundException(id);
        }

        byte[] content = checkReservation.printTicket(reservation);
        checkReservation.sendEmail(reservation);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add( "Content-Disposition", "filename=" + "reservation.txt" );

        ByteArrayResource resource = new ByteArrayResource(content);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(content.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
