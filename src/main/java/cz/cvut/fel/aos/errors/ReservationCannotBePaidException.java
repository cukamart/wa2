package cz.cvut.fel.aos.errors;


public class ReservationCannotBePaidException extends RuntimeException {
    private Long reservationId;

    public ReservationCannotBePaidException(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
