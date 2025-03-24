package app.testcontainers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestMongoDbContainer extends GenericContainer<TestMongoDbContainer> {
    public TestMongoDbContainer() {
        super(DockerImageName.parse("mongo:8.0.1"));
        withEnv("MONGO_INITDB_ROOT_USERNAME","ADMIN");
        withEnv("MONGO_INITDB_ROOT_PASSWORD","ADMINPASSWORD");
        withEnv("MONGO_INITDB_DATABASE","workerRental");
        withExposedPorts(27017);
    }

}