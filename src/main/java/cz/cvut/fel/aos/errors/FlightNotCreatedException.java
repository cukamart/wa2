package cz.cvut.fel.aos.errors;

/**
 * RuntimeException, nastane v pripade ze vytvarame let, ktory uz je v databaze.
 */
public class FlightNotCreatedException extends RuntimeException {
    private String name;

    public FlightNotCreatedException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
