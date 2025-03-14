package indie.outsource.WorkerRental.dtoMappers;

import indie.outsource.WorkerRental.model.Rent;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;

public final class RentMapper {
    public static RentDTO getRentDTO(Rent rent){
        return new RentDTO(rent.getStartDate(), rent.getEndDate(), rent.getUser().getId(), rent.getWorker().getId(), rent.getId());
    }

    static Rent getRentFromCreateRent(CreateRentDTO createRentDTO){
        Rent rent = new Rent();
        rent.setStartDate(createRentDTO.getStartDate());
        return rent;
    }

}
