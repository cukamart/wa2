package cz.cvut.fel.aos.controllers;

import cz.cvut.fel.aos.errors.*;
import cz.cvut.fel.aos.errors.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * ControllerAdvice odchyti specificke chyby pri praci s AirService Rest.
 * Funguje na zaklade aspektovo orientovaneho programovania, ak controller vyhodi exception
 * najde sa ControllerAdvice a prejde vsetky ExceptionHandlery a zisti ci sa da spracovat dana vynimka.
 */
@ControllerAdvice
@RestController
public class RestfullManagementErrorHandler {

    /**
     * ExceptionHandler, odchyti vynimky typu DestinationtNotFound
     *
     * @param e - DestinationNotFoundException obsahujuca spravu o chybe a chybovy kod
     * @return - vrati chybovu informujucu ze dana destinacia sa nenasla.
     */
    @ExceptionHandler(DestinationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error destinationNotFound(DestinationNotFoundException e) {
        return new Error(HttpStatus.NOT_FOUND.value(), "Destination [" + e.getDestinationId() + "] not found");
    }

    /**
     * ExceptionHandler, odchyti vynimky typu DestinationNotCreatedException
     *
     * @param e - DestinationNotCreatedException obsahujuca spravu o chybe a chybovy kod
     * @return - vrati chybovu informujucu ze sa dana destinacia uz v databaze nachadza.
     */
    @ExceptionHandler(DestinationNotCreatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error destinationNotCreated(DestinationNotCreatedException e) {
        return new Error(HttpStatus.CONFLICT.value(), "Destination with name [" + e.getName() + "] can't be created. Destination already exists");
    }

    /**
     * ExceptionHandler, odchyti vynimky typu FlightNotFound
     * @param e - FlightNotFoundException obsahujuca spravu o chybe a chybovy kod
     * @return - vrati chybovu informujucu ze dany let sa nenasiel.
     */
    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error flightNotFound(FlightNotFoundException e) {
        return new Error(HttpStatus.NOT_FOUND.value(), "Flight [" + e.getFlightId() + "] not found");
    }

    /**
     * ExceptionHandler, odchyti vynimky typu FlightNotCreatedException
     *
     * @param e - FlightNotCreatedException obsahujuca spravu o chybe a chybovy kod
     * @return - vrati chybovu informujucu ze sa dany let uz v databaze nachadza.
     */
    @ExceptionHandler(FlightNotCreatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error flightNotCreated(FlightNotCreatedException e) {
        return new Error(HttpStatus.CONFLICT.value(), "Flight with name [" + e.getName() + "] can't be created. Flight already exists");
    }

    /**
     * ExceptionHandler, odchyti vynimky typu ReservationtNotFound
     * @param e - ReservationNotFoundException obsahujuca spravu o chybe a chybovy kod
     * @return - vrati chybovu informujucu ze dana rezervacia sa nenasla.
     */
    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error reservationNotFound(ReservationNotFoundException e) {
        return new Error(HttpStatus.NOT_FOUND.value(), "Reservation [" + e.getReservationId() + "] not found");
    }

    /**
     * ExceptionHandler, odchyti vynimky typu FlightIsFullException
     * @param e - FlightIsFullException obsahujuca spravu o aktualnej obsadenosti lietadla
     * @return - vrati chybovu informujucu ze dana listok nebol zakupeny nakolko je let plny.
     */
    @ExceptionHandler(FlightIsFullException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Error flightIsFull(FlightIsFullException e) {
        return new Error(HttpStatus.NOT_ACCEPTABLE.value(), "Reservation cannot be done. " +
                "Flight is full. [" + e.getCurrentCapacity() + "/" + e.getMaxCapacity() +"] seats are already reserved.");
    }

    /**
     * ExceptionHandler, odchyti vynimky typu ReservationNotDeletableException
     * @param e - ReservationNotDeletableException oznami, ze nieje mozne vymazat uz zakupenu rezervaciu.
     * @return - vrati status 400 a chybovu informujucu ze dana rezervacia nemoze byt vymazana.
     */
    @ExceptionHandler(ReservationNotDeletableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error reservationNotDeletable(ReservationNotDeletableException e) {
        return new Error(HttpStatus.BAD_REQUEST.value(), "Reservation cannot be deleted. " +
                "Reservation with id [" + e.getReservationId() +"] is already paid.");
    }

    @ExceptionHandler(ReservationCannotBePaidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error reservationNotDeletable(ReservationCannotBePaidException e) {
        return new Error(HttpStatus.BAD_REQUEST.value(), "Reservation cannot be PAID. " +
                "Reservation with id [" + e.getReservationId() +"] is already paid or canceled." +
                "Please check state of your reservation via password.");
    }

    @ExceptionHandler(ReservationAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error reservationNotAccessible(ReservationAccessDeniedException e) {
        return new Error(HttpStatus.FORBIDDEN.value(), "Wrong password.");
    }
}
