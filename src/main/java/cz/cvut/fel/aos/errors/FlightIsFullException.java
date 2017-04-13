package cz.cvut.fel.aos.errors;

/**
 * RuntimeException, nastane v pripade ze kupujeme listok na let ktory je uz plny.
 */
public class FlightIsFullException extends RuntimeException {
    private Integer maxCapacity;
    private Integer currentCapacity;

    public FlightIsFullException(Integer currentCapacity, Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }
}
