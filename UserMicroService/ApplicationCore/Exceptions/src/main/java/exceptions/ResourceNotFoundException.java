package exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(ResourceNotFoundException e) {
        super(e);
    }
}
