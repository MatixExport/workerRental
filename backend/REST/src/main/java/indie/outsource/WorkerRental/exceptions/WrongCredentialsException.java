package indie.outsource.WorkerRental.exceptions;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(String message) {
        super(message);
    }

    public WrongCredentialsException() {
        super();
    }
}
