package connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoConnection {
    MongoDatabase getMongoDatabase();
    MongoClient getMongoClient();
}
