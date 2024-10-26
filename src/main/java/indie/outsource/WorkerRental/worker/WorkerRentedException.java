package indie.outsource.WorkerRental.worker;

public class WorkerRentedException extends RuntimeException {
    public WorkerRentedException(String message) {
        super(message);
    }

    public WorkerRentedException() {
    }
}
