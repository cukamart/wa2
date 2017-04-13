package cz.cvut.fel.aos.errors;

/**
 * RuntimeException, nastane v pripade ze sa odkazujeme na neexistujucu rezervaciu.
 */
public class ReservationNotFoundException extends RuntimeException {
    private Long reservationId;

    public ReservationNotFoundException(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
