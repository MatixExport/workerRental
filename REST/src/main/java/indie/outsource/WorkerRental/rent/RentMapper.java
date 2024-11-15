package indie.outsource.WorkerRental.rent;

import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;

public final class RentMapper {
    static RentDTO getRentDTO(Rent rent){
        return new RentDTO(rent.startDate, rent.endDate, rent.getUser().getId(), rent.getWorker().getId(), rent.getId());
    }

    static Rent getRentFromCreateRent(CreateRentDTO createRentDTO){
        Rent rent = new Rent();
        rent.setStartDate(createRentDTO.getStartDate());
        return rent;
    }

}
