package indie.outsource.WorkerRental.repositories;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import indie.outsource.WorkerRental.documents.RentMgd;
import indie.outsource.WorkerRental.documents.WorkerMgd;
import indie.outsource.WorkerRental.exceptions.WorkerRentedException;
import indie.outsource.WorkerRental.model.Rent;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;



@Component
@Repository
public class MongoRentRepositoryImpl extends BaseMongoRepository<RentMgd> implements RentRepository {
    public MongoRentRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, RentMgd.class);
    }

    @Override
    protected void createCollection() {
        super.createCollection();

    }

    private List<Rent> findByFilter(Bson filter) {
        return getCollection().find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(RentMgd::toDomainModel)
                .toList();
    }

    @Override
    public List<Rent> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        Document idFilter = new Document("user._id",id);
        //endDate < targetDate
        Document endDateFilter = new Document("endDate",new Document("$lt", date));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<Rent> findByUser_IdAndEndDateIsNull(UUID id) {
        Document idFilter = new Document("user._id",id);
        Document endDateFilter = new Document("endDate", new Document("$in", Arrays.asList(null, "")));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<Rent> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date) {
        Document idFilter = new Document("worker._id",id);
        //endDate < targetDate
        Document endDateFilter = new Document("endDate",new Document("$lt", date));
        return findByFilter(Filters.and(idFilter,endDateFilter));
    }

    @Override
    public List<Rent> findByWorker_IdAndEndDateIsNull(UUID id) {
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
    public List<Rent> findAll() {
        return mongoFindAll().stream()
                .map(RentMgd::toDomainModel)
                .toList();
    }

    @Override
    public Optional<Rent> findById(UUID id) {
        RentMgd mgd = mongoFindById(id);
        if (mgd != null) {return Optional.of(mgd.toDomainModel());}
        return Optional.empty();
    }


    private void updateIsRented(RentMgd rentMgd, int value){
        MongoCollection<WorkerMgd> workerMongoCollection = mongoConnection.getMongoDatabase().getCollection(WorkerMgd.class.getSimpleName(), WorkerMgd.class);
        Bson filter = Filters.eq("_id", rentMgd.getWorker().getId());
        Bson update = Updates.inc("isRented", value);
        workerMongoCollection.updateOne(filter, update);
    }


    @Override
    public Rent save(Rent rent) {
        RentMgd mgd = new RentMgd(rent);
        mgd.getUser().removePassword();
        if(rent.getId() == null){
            rent.setId(UUID.randomUUID());
            try{
                inSession(mongoConnection.getMongoClient(),()->{
                    updateIsRented(mgd, 1);
                    getCollection().insertOne(mgd);
                });
                return rent;
            }catch (MongoWriteException e){
                throw new WorkerRentedException();
            }
        }
        RentMgd dbMgd = mongoFindById(rent.getId());

        if((dbMgd != null)&&(dbMgd.getEndDate() == null)&&(rent.getEndDate() != null)){
            inSession(mongoConnection.getMongoClient(),()->{
                updateIsRented(dbMgd, -1);
                getCollection().replaceOne(new Document("_id", rent.getId()),mgd);
            });
            return rent;
        }
        getCollection().replaceOne(new Document("_id", rent.getId()),mgd);
        return rent;
    }

    @Override
    public void delete(Rent rent) {
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
