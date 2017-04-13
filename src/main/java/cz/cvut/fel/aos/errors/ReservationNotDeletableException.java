package cz.cvut.fel.aos.errors;

public class ReservationNotDeletableException extends RuntimeException {
    private Long reservationId;

    public ReservationNotDeletableException(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
