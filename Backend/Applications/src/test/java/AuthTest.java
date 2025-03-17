import infrastructure.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTest {
    private final static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private WorkerRepository workerRepository;

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.database", mongoDBContainer::getConnectionString);
    }

    @Test
    void test() {
        workerRepository.deleteAll();
    }







}
