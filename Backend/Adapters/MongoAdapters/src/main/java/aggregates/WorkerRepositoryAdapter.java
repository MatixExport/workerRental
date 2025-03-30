package aggregates;

import Entities.WorkerEnt;
import aggregates.mappers.WorkerMapper;
import documents.WorkerMgd;
import exceptions.WorkerRentedException;
import infrastructure.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repositories.interfaces.MongoWorkerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class WorkerRepositoryAdapter implements WorkerRepository {

    private final MongoWorkerRepository workerRepository;
    private final WorkerMapper workerMapper;

    @Override
    public List<WorkerEnt> findAll() {
        return workerMapper.toDomainModel(workerRepository.findAll());
    }

    @Override
    public Optional<WorkerEnt> findById(UUID id) {
        WorkerMgd workerMgd = workerRepository.findById(id);
        if (workerMgd != null) {return Optional.of(workerMapper.toDomainModel(workerMgd));}
        return Optional.empty();
    }

    @Override
    public WorkerEnt save(WorkerEnt worker) {
        return workerMapper.toDomainModel(workerRepository.save(workerMapper.toMongoModel(worker)));
    }

    @Override
    public void delete(WorkerEnt worker) throws WorkerRentedException {
        workerRepository.delete(workerMapper.toMongoModel(worker));
    }

    @Override
    public void deleteById(UUID id) throws WorkerRentedException {
        workerRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        workerRepository.deleteAll();
    }
}
