package cz.cvut.fel.aos.data.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fel.aos.jackson.views.View;
import cz.cvut.fel.aos.model.SessionIdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * POJO trieda pre persistentnu entitu Reservation.
 */
@Entity
@Table(name = "reservation")
@XmlRootElement
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @JsonView(View.ReservationSummary.class)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    @JsonView({View.ReservationSummary.class})
    private Flight flight;


    @JsonView({View.ReservationSummary.class})
    //@Temporal(TemporalType.DATE)
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date created;

    @NotNull
    @Size(min = 8, max = 45)
    @JsonView({View.ReservationSummary.class})
    private String password;

    @NotNull
    @JsonView({View.ReservationSummary.class})
    private Integer seats;

    @NotNull
    @JsonView({View.ReservationSummary.class})
    private State state = State.NEW;

    public Reservation() {
    }

    public Reservation(Flight flight, Integer seats, State state) {
        this.flight = flight;
        this.seats = seats;
        this.state = state;
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
        password = SessionIdentifierGenerator.nextSessionId();
        state = State.NEW;
    }

    public Long getId() {
        return id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Date getCreated() {
        return created;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Reservation [id: " + id + ", FlightName: " + flight.getName() + " From: " + flight.getDestinationFrom().getName() + " To: " + flight.getDestinationTo().getName() + " ]";
    }
}
