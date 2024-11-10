package indie.outsource.WorkerRental.rent;

import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;

public final class RentMapper {
    RentDTO getRentDTO(Rent rent){
        return new RentDTO(rent.startDate, rent.endDate, rent.getUser().getId(), rent.getWorker().getId());
    }

    Rent getRentFromRentDTO(RentDTO rentDTO){
        Rent rent = new Rent();
        rent.setStartDate(rentDTO.getStartDate());
        return rent;
    }

    Rent getRentFromCreateRent(CreateRentDTO createRentDTO){
        Rent rent = new Rent();
        rent.setStartDate(createRentDTO.getStartDate());
        return rent;
    }

}
