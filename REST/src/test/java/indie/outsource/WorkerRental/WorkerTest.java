package indie.outsource.WorkerRental;

import io.quarkus.test.junit.QuarkusTest;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import indie.outsource.WorkerRental.repositories.RentRepository;
import indie.outsource.WorkerRental.repositories.UserRepository;
import indie.outsource.WorkerRental.DTO.worker.CreateWorkerDTO;
import indie.outsource.WorkerRental.DTO.worker.WorkerDTO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static indie.outsource.WorkerRental.requests.WorkerRequests.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class WorkerTest {

    @Inject
    UserRepository userRepository;
    @Inject
    WorkerRepository workerRepository;
    @Inject
    RentRepository rentRepository;

    @BeforeEach
    public void fullTeardown(){
        rentRepository.deleteAll();
        userRepository.deleteAll();
        workerRepository.deleteAll();
    }

    @AfterEach
    public void teardown(){
        workerRepository.deleteAll();
    }

    @Test
    void createWorkerTest(){
        WorkerDTO worker = createDefaultWorker();

        assertEquals("Adam", worker.getName());

        WorkerDTO worker2 = getWorker(worker.getId());

        assertEquals(worker.getName(), worker2.getName());
    }

    @Test
    void readWorkerTest(){
        WorkerDTO worker = createDefaultWorker();

        WorkerDTO worker2 = getWorker(worker.getId());

        assertEquals(worker.getName(), worker2.getName());
    }

    @Test
    void updateWorkerTest(){
        WorkerDTO worker = createDefaultWorker();

        updateWorker(worker.getId(), new CreateWorkerDTO("Marek"));
        WorkerDTO worker2 = getWorker(worker.getId());
        assertEquals("Marek", worker2.getName());
    }

    @Test
    void deleteWorkerTest(){
        WorkerDTO worker = createDefaultWorker();
        deleteWorker(worker.getId());

        List<WorkerDTO> workers = getWorkers();

        assertEquals(0, workers.size());
    }

    @Test
    void tooShortWorkerNameTest(){
        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("A");
        given().contentType("application/json").
                body(createWorkerDTO).when().post("/workers").
                then().statusCode(400);
        assertEquals(0, getWorkers().size());
    }


}
