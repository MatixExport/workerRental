package aggregates;

import Entities.RentEnt;
import documents.RentMgd;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import infrastructure.RentRepository;
import lombok.AllArgsConstructor;
import aggregates.mappers.RentMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import repositories.interfaces.MongoRentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class RentRepositoryAdapter implements RentRepository {

    private final MongoRentRepository mongoRentRepository;
    private final RentMapper rentMapper;

    @Override
    public List<RentEnt> findAll() {
        return rentMapper.toDomainModel(mongoRentRepository.findAll());
    }

    @Override
    public Optional<RentEnt> findById(UUID id) {
        RentMgd rentMgd = mongoRentRepository.findById(id);
        if (rentMgd != null) {
            return Optional.of(rentMapper.toDomainModel(rentMgd));
        }
        return Optional.empty();
    }

    @Override
    public RentEnt save(RentEnt rent) throws ResourceNotFoundException, RentAlreadyEndedException, WorkerRentedException {
        return rentMapper.toDomainModel(mongoRentRepository.save(rentMapper.toMongoModel(rent)));
    }

    @Override
    public void delete(RentEnt rent) throws WorkerRentedException {
        mongoRentRepository.delete(rentMapper.toMongoModel(rent));
    }

    @Override
    public void deleteById(UUID id) {
        mongoRentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        mongoRentRepository.deleteAll();
    }

    @Override
    public List<RentEnt> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        return rentMapper.toDomainModel(mongoRentRepository.findByUser_IdAndEndDateBefore(id, date));
    }

    @Override
    public List<RentEnt> findByUser_IdAndEndDateIsNull(UUID id) {
        return rentMapper.toDomainModel(mongoRentRepository.findByUser_IdAndEndDateIsNull(id));
    }

    @Override
    public List<RentEnt> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        return rentMapper.toDomainModel(mongoRentRepository.findByWorker_IdAndEndDateBefore(id, date));
    }

    @Override
    public List<RentEnt> findByWorker_IdAndEndDateIsNull(UUID id) {
        return rentMapper.toDomainModel(mongoRentRepository.findByWorker_IdAndEndDateIsNull(id));
    }

    @Override
    public boolean existsByWorker_IdAndEndDateIsNull(UUID id) {
        return mongoRentRepository.existsByWorker_IdAndEndDateIsNull(id);
    }
}
