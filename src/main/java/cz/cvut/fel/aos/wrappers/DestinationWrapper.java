package cz.cvut.fel.aos.wrappers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.cvut.fel.aos.data.entities.Destination;
import cz.cvut.fel.aos.jackson.views.View;

import java.util.List;

/**
 * Wrapper pre list destinacii.
 */
@JacksonXmlRootElement(localName = "destinations")
public class DestinationWrapper {

    @JsonView(View.DestinationSummary.class)
    private List<Destination> destinations;

    public DestinationWrapper() {
    }

    public DestinationWrapper(List<Destination> destinations) {
        this.destinations = destinations;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "destination")
    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }
}
