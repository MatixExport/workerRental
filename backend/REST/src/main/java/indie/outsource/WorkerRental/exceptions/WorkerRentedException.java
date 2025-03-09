package indie.outsource.WorkerRental.exceptions;

public class WorkerRentedException extends RuntimeException {
    public WorkerRentedException(String message) {
        super(message);
    }

    public WorkerRentedException() {
    }
}
