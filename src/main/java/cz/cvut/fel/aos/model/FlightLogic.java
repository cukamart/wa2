package cz.cvut.fel.aos.model;

import cz.cvut.fel.aos.data.DestinationRepository;
import cz.cvut.fel.aos.data.entities.Destination;
import cz.cvut.fel.aos.data.entities.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Component
public class FlightLogic {

    private static final Logger log = LoggerFactory.getLogger(FlightLogic.class);

    @Autowired
    private DestinationRepository destinationData;

    public void calculatePriceOfFlight(Flight flight) {
        Destination fromDestination = flight.getDestinationFrom();
        Destination toDestination = flight.getDestinationTo();

        String oName = destinationData.findOne(fromDestination.getId()).getName();
        String dName = destinationData.findOne(toDestination.getId()).getName();

        String url = "http://free.rome2rio.com/api/1.2/json/Search?key=vwiC3pvW&oName=" + oName + "&dName=" + dName
                + "&noBus&noTrian&noCar";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        String responseBody = response.readEntity(String.class);
        response.close();  // You should close connections!


        JSONObject obj = new JSONObject(responseBody);
        Double distance = obj.getJSONArray("routes").getJSONObject(0).getDouble("distance");

        flight.setDistance(distance);
        //100 km = 1000 CZK.
        flight.setPrice(1000 * (distance/100));
        log.info("Prize of the flight {}", flight.getPrice());
    }
}
