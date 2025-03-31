package app.testcontainers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public class MongoTestContainer {
    private static final MongoDBContainer MONGO_DB_CONTAINER =  new MongoDBContainer("mongo:8.0.1");

    static {
        MONGO_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }
}
