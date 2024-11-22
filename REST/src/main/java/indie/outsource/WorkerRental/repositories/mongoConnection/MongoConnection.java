package indie.outsource.WorkerRental.repositories.mongoConnection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import indie.outsource.WorkerRental.documents.RentMgd;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import java.util.List;

@Getter
@ApplicationScoped
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

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.connection-string")
    String uri;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.credentials.username", defaultValue = "")
    String username;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.credentials.password", defaultValue = "")
    String password;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.credentials.auth-database", defaultValue = "")
    String authDatabase;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.database", defaultValue = "")
    String database;

    @PostConstruct
    public void init() {
        initDbConnection(uri, username, password, authDatabase, database);
    }

}
