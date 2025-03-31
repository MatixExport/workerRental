package repositories.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import documents.FieldsConsts;
import documents.users.UserMgd;
import exceptions.UserAlreadyExistsException;
import connection.MongoConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import repositories.interfaces.MongoUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MongoUserRepositoryImpl extends BaseMongoRepository<UserMgd> implements MongoUserRepository {
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
            new Document(FieldsConsts.ACCOUNT_LOGIN,1),indexOptions
        );
        collection.createIndex(new Document(FieldsConsts.ACCOUNT_LOGIN,"text"));
    }

    @Override
    public UserMgd findByLogin(String login) {
        return getCollection().find(new Document(FieldsConsts.ACCOUNT_LOGIN, login)).first();
    }


    //https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/indexes/
    //https://www.mongodb.com/docs/manual/core/link-text-indexes/#std-label-text-search-on-premises
    @Override
    public List<UserMgd> findByLoginContainsIgnoreCase(String login) {
        String searchQuery = ".*" + login + ".*";
        Bson regexFilter = Filters.regex(FieldsConsts.ACCOUNT_LOGIN,searchQuery , "i");
        return getCollection().find(regexFilter)
                .into(new ArrayList<>());
    }

    @Override
    public List<UserMgd> findAll() {
        return mongoFindAll();
    }

    @Override
    public UserMgd findById(UUID id) {
        return mongoFindById(id);
    }

    @Override
    public UserMgd save(UserMgd user) throws UserAlreadyExistsException {
        try{
            return mongoSave(user);
        }
        catch (MongoWriteException e){
            if(e.getError().getCode() == 11000){
                throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
            }
            throw e;
        }
    }

    @Override
    public UserMgd update(UserMgd user) {
        return mongoSave(user);
    }

    @Override
    public void delete(UserMgd user) {
        mongoDelete(user);
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
