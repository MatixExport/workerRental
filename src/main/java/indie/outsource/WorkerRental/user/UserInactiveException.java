package indie.outsource.WorkerRental.user;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String message) {
        super(message);
    }

    public UserInactiveException() {
    }
}
