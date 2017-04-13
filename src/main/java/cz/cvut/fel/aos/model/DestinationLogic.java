package cz.cvut.fel.aos.model;

import cz.cvut.fel.aos.data.entities.Destination;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Component
public class DestinationLogic {

    public Destination addLocationToDestination(Destination destination) {

        String name = destination.getName();

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + name
                + "&key=AIzaSyCmM1sL2reeTo8aELizEX2w9qX4dp7t7Lc";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        String responseBody = response.readEntity(String.class);
        response.close();  // You should close connections!

        JSONObject obj = new JSONObject(responseBody);
        Double lat = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        Double lng = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

        destination.setLat(lat);
        destination.setLon(lng);

        return destination;
    }

}
