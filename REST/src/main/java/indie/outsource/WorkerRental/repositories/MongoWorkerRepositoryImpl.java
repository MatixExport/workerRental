package indie.outsource.WorkerRental.repositories;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import indie.outsource.WorkerRental.documents.WorkerMgd;
import indie.outsource.WorkerRental.exceptions.WorkerRentedException;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Singleton
public class MongoWorkerRepositoryImpl extends BaseMongoRepository<WorkerMgd> implements WorkerRepository {

    @Inject
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

    private void updateIsRented(UUID workerUUID, int value){
        Bson filter = Filters.eq("_id", workerUUID);
        Bson update = Updates.inc("isRented", value);
        getCollection().updateOne(filter, update);
    }


    @Override
    public Optional<Worker> findById(UUID id) {
        WorkerMgd mgd = mongoFindById(id);
        if (mgd != null) {return Optional.of(mgd.toDomainModel());}
        return Optional.empty();
    }

    @Override
    public Worker save(Worker worker) {
        WorkerMgd mgd = new WorkerMgd(worker);
        if(worker.getId() == null) {
            mgd.setIsRented(0);
        }
       return mongoSave(mgd).toDomainModel();
    }

    @Override
    public void delete(Worker worker) {
        deleteById(worker.getId());
    }

    @Override
    public void deleteById(UUID t) {
        try{
            //If this is able to increment isRented value to 1
            //then that means that this worker was unallocated
            updateIsRented(t, 1);
            mongoDeleteById(t);
        }catch (MongoWriteException e){
            throw new WorkerRentedException();
        }
    }

    @Override
    public void deleteAll() {
        mongoDeleteAll();
    }
}
