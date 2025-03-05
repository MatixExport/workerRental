package repositories;

import Entities.RentEnt;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import documents.RentMgd;
import documents.WorkerMgd;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import infrastructure.RentRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import repositories.mongoConnection.MongoConnection;

import java.time.LocalDateTime;
import java.util.*;


@Repository
public class MongoRentRepositoryImpl extends BaseMongoRepository<RentMgd> implements RentRepository {
    public MongoRentRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, RentMgd.class);
    }

    @Override
    protected void createCollection() {
        super.createCollection();

    }

    private List<RentEnt> findByFilter(Bson filter) {
        return getCollection().find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(RentMgd::toDomainModel)
                .toList();
    }

    @Override
    public List<RentEnt> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        Document idFilter = new Document("Entities.user._id",id);
        //endDate < targetDate
        Document endDateFilter = new Document("endDate",new Document("$lt", date));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<RentEnt> findByUser_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document("Entities.user._id",id);
        Document endDateFilter = new Document("endDate", new Document("$in", Arrays.asList(null, "")));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<RentEnt> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        Document idFilter = new Document("worker._id",id);
        //endDate < targetDate
        Document endDateFilter = new Document("endDate",new Document("$lt", date));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<RentEnt> findByWorker_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document("worker._id",id);
        Document endDateFilter = new Document("endDate", new Document("$in", Arrays.asList(null, "")));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public boolean existsByWorker_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document("worker._id",id);
        Document endDateFilter = new Document("endDate", new Document("$in", Arrays.asList(null, "")));
        return getCollection().countDocuments(Filters.and(idFilter,endDateFilter)) > 0;
    }

    @Override
    public List<RentEnt> findAll() {
        return mongoFindAll().stream()
                .map(RentMgd::toDomainModel)
                .toList();
    }

    @Override
    public Optional<RentEnt> findById(UUID id) {
        RentMgd mgd = mongoFindById(id);
        if (mgd != null) {return Optional.of(mgd.toDomainModel());}
        return Optional.empty();
    }


    private void updateIsRented(WorkerMgd workerMgd, int value){
        MongoCollection<WorkerMgd> workerMongoCollection = mongoConnection.getMongoDatabase().getCollection(WorkerMgd.class.getSimpleName(), WorkerMgd.class);
        Bson filter = Filters.eq("_id", workerMgd.getId());
        Bson update = Updates.inc("isRented", value);
        workerMongoCollection.updateOne(filter, update);
    }


    @Override
    public RentEnt save(RentEnt rent) throws ResourceNotFoundException, RentAlreadyEndedException, WorkerRentedException {
        if(rent.getId() == null){
            rent.setId(UUID.randomUUID());
            return insert(rent);
        }
        RentMgd rentFromDB = mongoFindById(rent.getId());
        if(rentFromDB == null){
            throw new ResourceNotFoundException();
        }
        if(rentFromDB.getEndDate() != null){
            throw new RentAlreadyEndedException();
        }
        return update(rent);
    }

    private RentEnt insert(RentEnt rent) throws WorkerRentedException {
        RentMgd rentMgd = new RentMgd(rent);
        rentMgd.getUser().removePassword();
        try{
            inSession(mongoConnection.getMongoClient(),()->{
                updateIsRented(rentMgd.getWorker(), 1);
                getCollection().insertOne(rentMgd);
            });
            return rent;
        }catch (MongoWriteException e){
            throw new WorkerRentedException();
        }
    }

    private RentEnt update(RentEnt rent){
        if(rent.getEndDate() != null){
            return finish(rent);
        }
        RentMgd rentMgd = new RentMgd(rent);
        rentMgd.getUser().removePassword();
        getCollection().replaceOne(new Document("_id", rent.getId()),rentMgd);
        return rent;
    }

    private RentEnt finish(RentEnt rent){
        RentMgd rentMgd = new RentMgd(rent);
        rentMgd.getUser().removePassword();
        inSession(mongoConnection.getMongoClient(),()->{
            updateIsRented(rentMgd.getWorker(), -1);
            getCollection().replaceOne(new Document("_id", rent.getId()),rentMgd);
        });
        return rent;
    }

    @Override
    public void delete(RentEnt rent) {
        mongoDelete(new RentMgd(rent));
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
