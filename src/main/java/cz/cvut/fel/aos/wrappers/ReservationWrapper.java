package cz.cvut.fel.aos.wrappers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.cvut.fel.aos.data.entities.Reservation;
import cz.cvut.fel.aos.jackson.views.View;

import java.util.List;

/**
 * Wrapper pre list rezervacii.
 */
@JacksonXmlRootElement(localName="reservations")
public class ReservationWrapper {

    @JsonView(View.ReservationSummary.class)
    private List<Reservation> reservations;

    public ReservationWrapper() {}

    public ReservationWrapper(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName="reservation")
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
