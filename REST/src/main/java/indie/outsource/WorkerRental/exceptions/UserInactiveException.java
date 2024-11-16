package indie.outsource.WorkerRental.exceptions;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String message) {
        super(message);
    }

    public UserInactiveException() {
    }
}
