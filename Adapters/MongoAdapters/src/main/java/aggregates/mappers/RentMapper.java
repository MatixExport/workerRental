package aggregates.mappers;

import Entities.RentEnt;
import documents.RentMgd;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RentMapper {

    private final UserMapper userMapper;
    private final WorkerMapper workerMapper;

    public RentMgd toMongoModel(RentEnt rent){
        return new RentMgd(
                rent.getId(),
                rent.getStartDate(),
                rent.getEndDate(),
                workerMapper.toMongoModel(rent.getWorker()),
                userMapper.toMongoModel(rent.getUser()));
    }

    public RentEnt toDomainModel(RentMgd rent){
        RentEnt rentEnt = new RentEnt(
                rent.getStartDate(),
                rent.getEndDate(),
                workerMapper.toDomainModel(rent.getWorker()),
                userMapper.toDomainModel(rent.getUser()));

        rentEnt.setId(rent.getId());
        return rentEnt;
    }

    public List<RentEnt> toDomainModel(List<RentMgd> rents){
        return rents.stream().map(this::toDomainModel).toList();
    }
    public List<RentMgd> toMongoModel(List<RentEnt> rents){
        return rents.stream().map(this::toMongoModel).toList();
    }
}
