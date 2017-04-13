package cz.cvut.fel.aos.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fel.aos.jackson.views.View;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * POJO trieda pre persistentnu entitu Flight.
 */
@Entity
@Table(name = "flight")
@XmlRootElement
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Flight {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @JsonView({View.FlightSummary.class, View.ReservationSummary.class})
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flight")
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne
    @JsonView(View.FlightSummary.class)
    @JoinColumn(name = "from_destination_id", referencedColumnName = "id")
    private Destination destinationFrom;

    @ManyToOne
    @JsonView(View.FlightSummary.class)
    @JoinColumn(name = "to_destination_id", referencedColumnName = "id")
    private Destination destinationTo;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonView(View.FlightSummary.class)
    private Date dateOfDeparture;

    @NotNull
    @JsonView(View.FlightSummary.class)
    private Double distance;

    @NotNull
    @JsonView(View.FlightSummary.class)
    private Double price;

    @NotNull
    @JsonView(View.FlightSummary.class)
    private Integer seats;

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 45)
    @JsonView(View.FlightSummary.class)
    private String name;

    public Flight() {
    }

    public Flight(List<Reservation> reservations, Destination destinationFrom, Destination destinationTo, Date dateOfDeparture, Double distance, Double price, Integer seats, String name) {
        this.reservations = reservations;
        this.destinationFrom = destinationFrom;
        this.destinationTo = destinationTo;
        this.dateOfDeparture = dateOfDeparture;
        this.distance = distance;
        this.price = price;
        this.seats = seats;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Destination getDestinationFrom() {
        return destinationFrom;
    }

    public void setDestinationFrom(Destination destinationFrom) {
        this.destinationFrom = destinationFrom;
    }

    public Destination getDestinationTo() {
        return destinationTo;
    }

    public void setDestinationTo(Destination destinationTo) {
        this.destinationTo = destinationTo;
    }

    public Date getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(Date dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
