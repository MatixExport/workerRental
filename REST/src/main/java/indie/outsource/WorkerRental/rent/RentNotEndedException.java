package indie.outsource.WorkerRental.rent;

public class RentNotEndedException extends RuntimeException {
    public RentNotEndedException(String message) {
        super(message);
    }

    public RentNotEndedException() {
    }
}
