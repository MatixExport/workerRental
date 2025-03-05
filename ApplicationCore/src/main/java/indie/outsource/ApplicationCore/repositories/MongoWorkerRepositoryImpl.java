package indie.outsource.ApplicationCore.repositories;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import indie.outsource.ApplicationCore.documents.WorkerMgd;
import indie.outsource.ApplicationCore.exceptions.WorkerRentedException;
import indie.outsource.ApplicationCore.model.Worker;
import indie.outsource.ApplicationCore.repositories.mongoConnection.MongoConnection;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
