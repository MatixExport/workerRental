package exceptions;

public class UserInactiveException extends Exception {
    public UserInactiveException(String message) {
        super(message);
    }

    public UserInactiveException() {
    }
}
