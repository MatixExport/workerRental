package app.Soap;

import app.helper.SoapWorkerClient;
import app.testcontainers.MongoTestContainer;
import com.example.soap.Worker;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SoapWorkerTest extends MongoTestContainer {

    private static SoapWorkerClient workerClient;

    @BeforeAll
    static void beforeAll() {
        workerClient = new SoapWorkerClient();
    }

    @Test
    void createWorkerTest() {
        Worker worker = workerClient.createWorker("best worker").getWorker();

        Worker worker2 = workerClient.getWorker(worker.getId()).getWorker();

        Assertions.assertEquals("best worker", worker2.getName());
    }

    @Test
    void updateWorkerTest() {
        Worker worker = workerClient.createWorker("best worker").getWorker();

        Worker worker2 =  workerClient.updateWorker(worker.getId(), "best worker updated").getWorker();
        Assertions.assertEquals("best worker updated", worker2.getName());

        Worker worker3 = workerClient.getWorker(worker.getId()).getWorker();
        Assertions.assertEquals("best worker updated", worker3.getName());
    }

    @Test
    void deleteWorkerTest() {
        Worker worker = workerClient.createWorker("best worker").getWorker();

        workerClient.deleteWorker(worker.getId());

        Worker worker2 = workerClient.getWorker(worker.getId()).getWorker();
        Assertions.assertNull(worker2);
    }
}
