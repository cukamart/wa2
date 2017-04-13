package cz.cvut.fel.aos.wrappers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.cvut.fel.aos.data.entities.Flight;
import cz.cvut.fel.aos.jackson.views.View;

import java.util.List;

/**
 * Wrapper pre list letov.
 */
@JacksonXmlRootElement(localName="flights")
public class FlightWrapper {

    @JsonView(View.FlightSummary.class)
    private List<Flight> flights;

    public FlightWrapper() {}

    public FlightWrapper(List<Flight> flights) {
        this.flights = flights;
    }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName="flight")
    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
