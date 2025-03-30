package repositories.implementations;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import documents.AbstractEntityMgd;
import documents.FieldsConsts;
import mongoConnection.MongoConnection;
import mongoConnection.MongoSchema;
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
    }


    protected void createCollection() {
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(
                MongoSchema.getSchema(entityClass.getSimpleName())
        );
        mongoConnection.getMongoDatabase().createCollection(entityClass.getSimpleName(), createCollectionOptions);
    }

    protected List<T> mongoFindAll() {
        return getCollection().find().into(new ArrayList<>());
    }


    protected T mongoFindById(UUID id) {
        return getCollection().find(new Document(FieldsConsts.ENTITY_ID, id)).first();
    }

    protected T mongoSave(T t) {
        if(t.getId() == null){
            t.setId(UUID.randomUUID());
        }
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        Bson filter = new Document(FieldsConsts.ENTITY_ID, t.getId());
        UpdateResult result = getCollection().replaceOne(filter, t, options);
        if(!result.wasAcknowledged()){
            //Exception
            return null;
        }

        return t;
    }

    protected void mongoDelete(T t) {
        getCollection().deleteOne(new Document(FieldsConsts.ENTITY_ID,t.getId()));
    }


    protected void mongoDeleteById(UUID t) {
        getCollection().deleteOne(new Document(FieldsConsts.ENTITY_ID,t));
    }


    protected void mongoDeleteAll() {
        getCollection().deleteMany(new Document());
    }

    protected void inSession(MongoClient client, Runnable work) {
        ClientSession session = client.startSession();
        try {
            session.startTransaction();
            work.run();
            session.commitTransaction();
        }
        catch (Exception e) {
            session.abortTransaction();
            throw e;
        }
        finally {
            session.close();
        }
    }
}
