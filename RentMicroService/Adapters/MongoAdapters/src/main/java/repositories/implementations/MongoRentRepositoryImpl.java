package repositories.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import documents.FieldsConsts;
import documents.RentMgd;
import documents.WorkerMgd;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import connection.CredentialsMongoConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import repositories.interfaces.MongoRentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public class MongoRentRepositoryImpl extends BaseMongoRepository<RentMgd> implements MongoRentRepository {
    public MongoRentRepositoryImpl(CredentialsMongoConnection mongoConnection) {
        super(mongoConnection, RentMgd.class);
    }

    private List<RentMgd> findByFilter(Bson filter) {
        return getCollection().find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        Document idFilter = new Document(FieldsConsts.RENT_USER +"."+FieldsConsts.ENTITY_ID,id);
        //endDate < targetDate
        Document endDateFilter = new Document(FieldsConsts.RENT_END_DATE,new Document("$lt", date));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<RentMgd> findByUser_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document(FieldsConsts.RENT_USER +"."+FieldsConsts.ENTITY_ID,id);
        Document endDateFilter = new Document(FieldsConsts.RENT_END_DATE, new Document("$in", Arrays.asList(null, "")));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<RentMgd> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        Document idFilter = new Document(FieldsConsts.RENT_WORKER +"."+FieldsConsts.ENTITY_ID,id);
        //endDate < targetDate
        Document endDateFilter = new Document(FieldsConsts.RENT_END_DATE,new Document("$lt", date));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<RentMgd> findByWorker_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document(FieldsConsts.RENT_WORKER +"."+FieldsConsts.ENTITY_ID,id);
        Document endDateFilter = new Document(FieldsConsts.RENT_END_DATE, new Document("$in", Arrays.asList(null, "")));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public boolean existsByWorker_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document(FieldsConsts.RENT_WORKER +"."+FieldsConsts.ENTITY_ID,id);
        Document endDateFilter = new Document(FieldsConsts.RENT_END_DATE, new Document("$in", Arrays.asList(null, "")));
        return getCollection().countDocuments(Filters.and(idFilter,endDateFilter)) > 0;
    }

    @Override
    public List<RentMgd> findAll() {
        return mongoFindAll();
    }

    @Override
    public RentMgd findById(UUID id) {
        return mongoFindById(id);
    }

    private void updateIsRented(WorkerMgd workerMgd, int value){
        MongoCollection<WorkerMgd> workerMongoCollection = mongoConnection.getMongoDatabase().getCollection(WorkerMgd.class.getSimpleName(), WorkerMgd.class);
        Bson filter = Filters.eq(FieldsConsts.ENTITY_ID, workerMgd.getId());
        Bson update = Updates.inc(FieldsConsts.WORKER_IS_RENTED, value);
        workerMongoCollection.updateOne(filter, update);
    }

    @Override
    public RentMgd save(RentMgd rent) throws ResourceNotFoundException, RentAlreadyEndedException, WorkerRentedException {
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

    private RentMgd insert(RentMgd rent) throws WorkerRentedException {
        try{
            inSession(mongoConnection.getMongoClient(),()->{
                updateIsRented(rent.getWorker(), 1);
                getCollection().insertOne(rent);
            });
            return rent;
        }catch (MongoWriteException e){
            throw new WorkerRentedException();
        }
    }

    private RentMgd update(RentMgd rent){
        if(rent.getEndDate() != null){
            return finish(rent);
        }
        getCollection().replaceOne(new Document(FieldsConsts.ENTITY_ID, rent.getId()),rent);
        return rent;
    }

    private RentMgd finish(RentMgd rent){
        inSession(mongoConnection.getMongoClient(),()->{
            updateIsRented(rent.getWorker(), -1);
            getCollection().replaceOne(new Document(FieldsConsts.ENTITY_ID, rent.getId()),rent);
        });
        return rent;
    }

    @Override
    public void delete(RentMgd rent) {
        mongoDelete(rent);
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
