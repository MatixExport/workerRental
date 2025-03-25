package exceptions;

public class WorkerRentedException extends Exception {
    public WorkerRentedException(String message) {
        super(message);
    }

    public WorkerRentedException() {
    }
}
