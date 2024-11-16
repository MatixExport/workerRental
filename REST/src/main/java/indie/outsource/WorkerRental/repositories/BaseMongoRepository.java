package indie.outsource.WorkerRental.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import indie.outsource.WorkerRental.documents.AbstractEntityMgd;
import indie.outsource.WorkerRental.documents.user.UserMgd;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import indie.outsource.WorkerRental.repositories.mongoConnection.MongoSchema;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BaseMongoRepository<T extends AbstractEntityMgd>{
    MongoConnection mongoConnection;
    MongoCollection<T> collection;
    Class<T> entityClass;


    public BaseMongoRepository(MongoConnection mongoConnection, Class<T> entityClass) {
        this.mongoConnection = mongoConnection;
        this.entityClass = entityClass;
        if(!mongoConnection.getMongoDatabase().listCollectionNames()
                .into(new ArrayList<>())
                .contains(entityClass.getSimpleName())){
            createCollection();
        }
        collection = mongoConnection.getMongoDatabase().getCollection(entityClass.getSimpleName(),entityClass);
    }

    protected MongoCollection<T> getCollection() {
          return collection;
    };

    protected MongoConnection getMongoConnection() {
        return mongoConnection;
    }

    protected void createCollection() {
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(
                MongoSchema.getSchema(entityClass.getSimpleName())
        );
        mongoConnection.getMongoDatabase().createCollection(entityClass.getSimpleName(), createCollectionOptions);
    }

    protected List<T> mongoFindAll() {
        return getCollection().find().into(new ArrayList<T>());
    }


    protected T mongoFindById(UUID id) {
        return getCollection().find(new Document("_id", id)).first();
    }

    protected T mongoSave(T t) {
        //todo: change to return actual new id
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        Bson filter = new Document("_id", t.getId());
        getCollection().replaceOne(filter, t, options);
        return t;
    }

    protected void mongoDelete(T t) {
        getCollection().deleteOne(new Document("_id",t.getId()));
    }


    protected void mongoDeleteById(UUID t) {
        getCollection().deleteOne(new Document("_id",t));
    }


    protected void mongoDeleteAll() {
        getCollection().deleteMany(new Document());
    }
}
