package cz.cvut.fel.aos.errors;

/**
 * RuntimeException, nastane v pripade ze vytvarame destinaciu, ktora uz v databaze je.
 */
public class DestinationNotCreatedException extends RuntimeException {
    private String name;

    public DestinationNotCreatedException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
