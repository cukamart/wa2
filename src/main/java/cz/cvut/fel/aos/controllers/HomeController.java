package cz.cvut.fel.aos.controllers;

import cz.cvut.fel.aos.data.DestinationRepository;
import cz.cvut.fel.aos.data.FlightRepository;
import cz.cvut.fel.aos.data.ReservationRepository;
import cz.cvut.fel.aos.data.entities.Destination;
import cz.cvut.fel.aos.data.entities.Flight;
import cz.cvut.fel.aos.data.entities.Reservation;
import cz.cvut.fel.aos.data.entities.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Basic controller, sluzi na odtestovanie ci nabehol spravne server pripadne na naplnenie databazy.
 */
@Controller
public class HomeController {

    @Autowired
    private DestinationRepository destinationData;

    @Autowired
    private FlightRepository flightData;

    @Autowired
    private ReservationRepository reservationData;

    /**
     * Po starte aplikacneho serveru zobrazi hello world stranku.
     * @return index - uvodna obrazovka po starte serveru
     */
    @RequestMapping("/")
    public String firstPage() {
        return "index";
    }

    /**
     * Naplni databazu preddefinovanymi datami.
     * @return databasePopulated - obrazovka informujuca o naplneni databazy
     */
    @RequestMapping("/fill")
    public String fill() throws ParseException {
        reservationData.deleteAll();
        flightData.deleteAll();
        destinationData.deleteAll();

        Destination destinationRome = new Destination();

        destinationRome.setName("Rome");
        destinationRome.setLat(45.12);
        destinationRome.setLon(39.12);

        destinationData.save(destinationRome);

        Destination destinationPrague = new Destination();

        destinationPrague.setName("Prague");
        destinationPrague.setLat(40.14);
        destinationPrague.setLon(32.47);

        destinationData.save(destinationPrague);

        Destination destinationFlorida = new Destination();

        destinationFlorida.setName("Florida");
        destinationFlorida.setLat(41.12);
        destinationFlorida.setLon(30.12);

        destinationData.save(destinationFlorida);

        Destination destinationDubai = new Destination();

        destinationDubai.setName("Dubai");
        destinationDubai.setLat(16.14);
        destinationDubai.setLon(18.47);

        destinationData.save(destinationDubai);

        Destination destinationAmsterdam = new Destination();

        destinationAmsterdam.setName("Amsterdam");
        destinationAmsterdam.setLat(13.13);
        destinationAmsterdam.setLon(29.44);

        destinationData.save(destinationAmsterdam);

        Flight flight = new Flight();

        flight.setName("flight1");
        flight.setSeats(350);
        flight.setPrice(399.99);
        flight.setDistance(450.07);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = sdf.parse("21/12/2012");
        flight.setDateOfDeparture(d);
        flight.setDestinationFrom(destinationRome);
        flight.setDestinationTo(destinationPrague);
        destinationPrague.getFlightsTo().add(flight);
        destinationRome.getFlightsFrom().add(flight);

        flightData.save(flight);

        Reservation reservation = new Reservation();

        reservation.setSeats(1);
        reservation.setFlight(flight);
        flight.getReservations().add(reservation);

        reservationData.save(reservation);

        Reservation reservation2 = new Reservation();

        reservation2.setSeats(1);
        reservation2.setFlight(flight);
        flight.getReservations().add(reservation2);

        reservationData.save(reservation2);

        return "databasePopulated";
    }
}
