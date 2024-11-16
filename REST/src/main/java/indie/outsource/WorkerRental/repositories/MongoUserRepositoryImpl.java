package indie.outsource.WorkerRental.repositories;

import com.mongodb.client.model.Filters;
import indie.outsource.WorkerRental.documents.WorkerMgd;
import indie.outsource.WorkerRental.documents.user.UserMgd;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Component
public class MongoUserRepositoryImpl extends BaseMongoRepository<UserMgd> implements UserRepository {
    @Autowired
    public MongoUserRepositoryImpl(MongoConnection mongoConnection) {
        super(mongoConnection, UserMgd.class);

        if(mongoConnection.getMongoDatabase().listCollectionNames()
                .into(new ArrayList<>())
                .contains(UserMgd.class.getSimpleName())){
            return;
        }

    }

    @Override
    public Optional<User> findByLogin(String login) {
        UserMgd mgd = getCollection().find(new Document("login", login)).first();
        if (mgd != null) {
            return Optional.of(mgd.toDomainModel());
        }
        return Optional.empty();
    }


    //https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/indexes/
    //https://www.mongodb.com/docs/manual/core/link-text-indexes/#std-label-text-search-on-premises
    //It could be faster
    @Override
    public List<User> findByLoginContainsIgnoreCase(String login) {
//        Document filter = Filters.text(
//
//        )
//        return getCollection().find(new Document("login", login)).into(new ArrayList<>());
        return List.of();
    }

    @Override
    public List<User> findAll() {
        return mongoFindAll().stream()
                .map(UserMgd::toDomainModel)
                .toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        UserMgd mgd = mongoFindById(id);
        if (mgd != null) {return Optional.of(mgd.toDomainModel());}
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        mongoSave(UserMgd.fromDomainModel(user));
        return user;
    }

    @Override
    public void delete(User user) {
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
