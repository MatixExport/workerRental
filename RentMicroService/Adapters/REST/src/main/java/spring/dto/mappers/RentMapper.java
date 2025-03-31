package spring.dto.mappers;


import entities.RentEnt;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;

public final class RentMapper {

    private RentMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static RentDTO getRentDTO(RentEnt rent){
        return new RentDTO(rent.getStartDate(), rent.getEndDate(), rent.getUser().getId(), rent.getWorker().getId(), rent.getId());
    }

    static RentEnt getRentFromCreateRent(CreateRentDTO createRentDTO){
        RentEnt rent = new RentEnt();
        rent.setStartDate(createRentDTO.getStartDate());
        return rent;
    }

}
