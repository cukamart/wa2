package cz.cvut.fel.aos.data.entities;

public enum State {
    NEW("new"), CANCELED("canceled"), PAID("paid");

    private final String value;

    private State(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }
}
