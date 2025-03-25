package exceptions;

public class RentNotEndedException extends Exception {
    public RentNotEndedException(String message) {
        super(message);
    }

    public RentNotEndedException() {
    }
}
