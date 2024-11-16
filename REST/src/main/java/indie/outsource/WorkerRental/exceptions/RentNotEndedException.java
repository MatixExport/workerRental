package indie.outsource.WorkerRental.exceptions;

public class RentNotEndedException extends RuntimeException {
    public RentNotEndedException(String message) {
        super(message);
    }

    public RentNotEndedException() {
    }
}
