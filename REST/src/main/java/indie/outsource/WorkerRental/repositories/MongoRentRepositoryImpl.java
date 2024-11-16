package indie.outsource.WorkerRental.repositories;

import indie.outsource.WorkerRental.documents.RentMgd;
import indie.outsource.WorkerRental.model.Rent;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Repository
public class MongoRentRepositoryImpl extends BaseMongoRepository<RentMgd> implements RentRepository {
    public MongoRentRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, RentMgd.class);
    }

    @Override
    public List<Rent> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Rent> findByUser_IdAndEndDateIsNull(UUID id) {
        return List.of();
    }

    @Override
    public List<Rent> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Rent> findByWorker_IdAndEndDateIsNull(UUID id) {
        return List.of();
    }

    @Override
    public boolean existsByWorker_IdAndEndDateIsNull(UUID id) {
        return false;
    }

    @Override
    public List<Rent> findAll() {
        return List.of();
    }

    @Override
    public Optional<Rent> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Rent save(Rent rent) {
        return null;
    }

    @Override
    public void delete(Rent rent) {

    }

    @Override
    public void deleteById(UUID t) {

    }

    @Override
    public void deleteAll() {

    }
}
