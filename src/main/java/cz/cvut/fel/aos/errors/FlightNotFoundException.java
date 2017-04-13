package cz.cvut.fel.aos.errors;

/**
 * RuntimeException, nastane v pripade ze sa odkazujeme na neexistujuci let.
 */
public class FlightNotFoundException extends RuntimeException {
    private Long flightId;

    public FlightNotFoundException(Long flightId) {
        this.flightId = flightId;
    }

    public Long getFlightId() {
        return flightId;
    }
}
