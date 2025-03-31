package connection;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@Primary
public class DefaultMongoConnection extends CredentialsMongoConnection {

    @Autowired
    public DefaultMongoConnection(Environment environment) {
        super(environment);
    }

    @Override
    protected void initDbConnection(String mongoConnectionPath, String username, String password, String authDatabaseName, String databaseName) {
        MongoClientSettings settings = withBasicSettings(mongoConnectionPath).build();
        setMongoClient(MongoClients.create(settings));
        setMongoDatabase(getMongoClient().getDatabase(databaseName));
    }
}
