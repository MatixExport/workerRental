package indie.outsource.WorkerRental.repositories;

import com.mongodb.client.MongoCollection;
import indie.outsource.WorkerRental.documents.WorkerMgd;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Repository
public class MongoWorkerRepositoryImpl extends BaseMongoRepository<WorkerMgd> implements WorkerRepository {

    @Autowired
    public MongoWorkerRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, WorkerMgd.class);
    }

    @Override
    protected void createCollection() {
        super.createCollection();

    }

    @Override
    public List<Worker> findAll() {
        return mongoFindAll().stream()
                .map(WorkerMgd::toDomainModel)
                .toList();
    }

    @Override
    public Optional<Worker> findById(UUID id) {
        WorkerMgd mgd = mongoFindById(id);
        if (mgd != null) {return Optional.of(mgd.toDomainModel());}
        return Optional.empty();
    }

    @Override
    public Worker save(Worker worker) {
       return mongoSave(new WorkerMgd(worker)).toDomainModel();
    }

    @Override
    public void delete(Worker worker) {
        mongoDelete(new WorkerMgd(worker));
    }

    @Override
    public void deleteById(UUID t) {
        mongoDeleteById(t);
    }

    @Override
    public void deleteAll() {
        mongoDeleteAll();
    }
}
