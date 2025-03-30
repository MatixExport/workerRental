package repositories.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import documents.FieldsConsts;
import documents.WorkerMgd;
import exceptions.WorkerRentedException;
import connection.MongoConnection;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import repositories.interfaces.MongoWorkerRepository;

import java.util.List;
import java.util.UUID;


@Repository
public class MongoWorkerRepositoryImpl extends BaseMongoRepository<WorkerMgd> implements MongoWorkerRepository {

    @Autowired
    public MongoWorkerRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, WorkerMgd.class);
    }

    @Override
    public List<WorkerMgd> findAll() {
        return mongoFindAll();
    }

    private void updateIsRented(UUID workerUUID, int value){
        Bson filter = Filters.eq(FieldsConsts.ENTITY_ID, workerUUID);
        Bson update = Updates.inc(FieldsConsts.WORKER_IS_RENTED, value);
        getCollection().updateOne(filter, update);
    }


    @Override
    public WorkerMgd findById(UUID id) {
        return mongoFindById(id);
    }

    @Override
    public WorkerMgd save(WorkerMgd worker) {
        if(worker.getId() == null) {
            worker.setIsRented(0);
        }
       return mongoSave(worker);
    }

    @Override
    public void delete(WorkerMgd worker) throws WorkerRentedException {
        deleteById(worker.getId());
    }

    @Override
    public void deleteById(UUID t) throws WorkerRentedException {
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
