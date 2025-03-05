package repositories.mongoConnection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import documents.RentMgd;
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
public class MongoConnection {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION,Conventions.CLASS_AND_PROPERTY_CONVENTION))
                    .register(RentMgd.class)
                    .build()
    );

    private void initDbConnection(String mongoConnectionPath,String username,String password,String authDatabaseName,String databaseName) {
        MongoClientSettings settings = MongoClientSettings.builder().
                credential(MongoCredential.createCredential(
                        username, authDatabaseName, password.toCharArray()
                )).
                applyConnectionString(new ConnectionString(mongoConnectionPath)).
                uuidRepresentation(UuidRepresentation.STANDARD).
                codecRegistry(
                        CodecRegistries.fromRegistries(
                                MongoClientSettings.getDefaultCodecRegistry(),
                                pojoCodecRegistry
                        )
                ).build();

        mongoClient = MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    @Autowired
    public MongoConnection(Environment environment) {
        initDbConnection(
                environment.getProperty("spring.data.mongodb.uri"),
                environment.getProperty("spring.data.mongodb.username"),
                environment.getProperty("spring.data.mongodb.password"),
                environment.getProperty("spring.data.mongodb.authentication-database"),
                environment.getProperty("spring.data.mongodb.database")
        );
    }

}
