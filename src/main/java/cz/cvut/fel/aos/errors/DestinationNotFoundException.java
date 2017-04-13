package cz.cvut.fel.aos.errors;

/**
 * RuntimeException, nastane v pripade ze sa odkazujeme na neexistujucu destinaciu.
 */
public class DestinationNotFoundException extends RuntimeException {
    private Long destinationId;

    public DestinationNotFoundException(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Long getDestinationId() {
        return destinationId;
    }
}
