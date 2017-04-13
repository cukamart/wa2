package cz.cvut.fel.aos.data.entities;


import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fel.aos.jackson.views.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO trieda pre persistentnu entitu Destination.
 */
@Entity
@Table(name = "destination")
@XmlRootElement
public class Destination {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @JsonView({View.FlightSummary.class, View.DestinationSummary.class})
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destinationFrom")
    private List<Flight> flightsFrom = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destinationTo")
    private List<Flight> flightsTo = new ArrayList<>();

    @NotNull
    @Column(unique = true)
    @JsonView(View.DestinationSummary.class)
    private String name;

    @JsonView(View.DestinationSummary.class)
    private Double lat;

    @JsonView(View.DestinationSummary.class)
    private Double lon;

    public Destination() {

    }

    /**
     * lat a lon sa vygeneruje cez API geogoole...
     */
    public Destination(List<Flight> flightsFrom, List<Flight> flightsTo, String name) {
        this.flightsFrom = flightsFrom;
        this.flightsTo = flightsTo;
        this.name = name;
    }

    public Destination(List<Flight> flightsFrom, List<Flight> flightsTo, String name, Double lat, Double lon) {
        this.flightsFrom = flightsFrom;
        this.flightsTo = flightsTo;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public List<Flight> getFlightsFrom() {
        if (this.flightsFrom == null) {
            this.flightsFrom = new ArrayList<>();
        }
        return flightsFrom;
    }

    public void setFlightsFrom(List<Flight> flightsFrom) {
        this.flightsFrom = flightsFrom;
    }

    public List<Flight> getFlightsTo() {
        if (this.flightsTo == null) {
            this.flightsTo = new ArrayList<>();
        }
        return flightsTo;
    }

    public void setFlightsTo(List<Flight> flightsTo) {
        this.flightsTo = flightsTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
