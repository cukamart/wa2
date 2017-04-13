package cz.cvut.fel.aos.jackson.views;

/**
 * Konfiguracna trieda pre Jackson.
 * Jackson defaultne serializuje vsetky atributy z persistencie, vdaka tejto triede mozme urcit,
 * ktore atributy chceme pri volani restu zobrazit a ktore nie.
 */
public class View {
    public interface FlightSummary {
    }

    public interface ReservationSummary {
    }

    public interface DestinationSummary {
    }
}
