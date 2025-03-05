package repositories;

import Entities.user.UserEnt;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import documents.user.UserMgd;
import exceptions.UserAlreadyExistsException;
import infrastructure.UserRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import repositories.mongoConnection.MongoConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoUserRepositoryImpl extends BaseMongoRepository<UserMgd> implements UserRepository {
    @Autowired
    public MongoUserRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, UserMgd.class);
    }

    @Override
    protected void createCollection() {
        super.createCollection();
        IndexOptions indexOptions = new IndexOptions()
                .unique(true)
                .collation(
                        Collation.builder()
                                .locale("en")
                                .collationStrength(CollationStrength.PRIMARY).build()
                );
        MongoCollection<UserMgd> collection = mongoConnection.getMongoDatabase().getCollection(UserMgd.class.getSimpleName(), UserMgd.class);
        collection.createIndex(
            new Document("login",1),indexOptions
        );
        collection.createIndex(new Document("login","text"));
    }

    @Override
    public Optional<UserEnt> findByLogin(String login) {
        UserMgd mgd = getCollection().find(new Document("login", login)).first();
        if (mgd != null) {
            return Optional.of(mgd.toDomainModel());
        }
        return Optional.empty();
    }


    //https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/indexes/
    //https://www.mongodb.com/docs/manual/core/link-text-indexes/#std-label-text-search-on-premises
    @Override
    public List<UserEnt> findByLoginContainsIgnoreCase(String login) {
        String searchQuery = ".*" + login + ".*";
        Bson regexFilter = Filters.regex("login",searchQuery , "i");
        return getCollection().find(regexFilter)
                .into(new ArrayList<>())
                .stream()
                .map(UserMgd::toDomainModel)
                .toList();
    }

    @Override
    public List<UserEnt> findAll() {
        return mongoFindAll().stream()
                .map(UserMgd::toDomainModel)
                .toList();
    }

    @Override
    public Optional<UserEnt> findById(UUID id) {
        UserMgd mgd = mongoFindById(id);
        if (mgd != null) {return Optional.of(mgd.toDomainModel());}
        return Optional.empty();
    }

    @Override
    public UserEnt save(UserEnt user) throws UserAlreadyExistsException {
        try{
            return mongoSave(UserMgd.fromDomainModel(user)).toDomainModel();
        }
        catch (MongoWriteException e){
            if(e.getError().getCode() == 11000){
                throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
            }
            throw e;
        }
    }

    @Override
    public void delete(UserEnt user) {
        mongoDelete(UserMgd.fromDomainModel(user));
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
