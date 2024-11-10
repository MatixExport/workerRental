package indie.outsource.WorkerRental.rent;

public class RentAlreadyEndedException extends RuntimeException {
    public RentAlreadyEndedException(String message) {
        super(message);
    }

    public RentAlreadyEndedException() {
    }
}
