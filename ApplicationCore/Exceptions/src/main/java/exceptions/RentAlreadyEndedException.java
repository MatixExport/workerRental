package exceptions;

public class RentAlreadyEndedException extends Exception {
    public RentAlreadyEndedException(String message) {
        super(message);
    }

    public RentAlreadyEndedException() {
    }
}
