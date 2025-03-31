package connection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Component
public class CredentialsMongoConnection implements MongoConnection {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    protected void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    protected void setMongoDatabase(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION,Conventions.CLASS_AND_PROPERTY_CONVENTION))
                    .build()
    );

    protected MongoClientSettings.Builder withBasicSettings(String mongoConnectionPath){
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoConnectionPath)).
                uuidRepresentation(UuidRepresentation.STANDARD).
                codecRegistry(
                        CodecRegistries.fromRegistries(
                                MongoClientSettings.getDefaultCodecRegistry(),
                                getPojoCodecRegistry()
                        )
                );
    }


    protected void initDbConnection(String mongoConnectionPath,String username,String password,String authDatabaseName,String databaseName) {
        MongoClientSettings settings = withBasicSettings(mongoConnectionPath)
          .credential(MongoCredential.createCredential(
                username, authDatabaseName, password.toCharArray()
        ))
       .build();
        setMongoClient(MongoClients.create(settings));
        setMongoDatabase(getMongoClient().getDatabase(databaseName));
    }

    @Autowired
    public CredentialsMongoConnection(Environment environment) {
        initDbConnection(
                environment.getProperty("spring.data.mongodb.uri"),
                environment.getProperty("spring.data.mongodb.username"),
                environment.getProperty("spring.data.mongodb.password"),
                environment.getProperty("spring.data.mongodb.authentication-database"),
                environment.getProperty("spring.data.mongodb.database")
        );
    }

}
