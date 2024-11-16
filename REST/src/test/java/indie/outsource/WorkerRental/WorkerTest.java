package indie.outsource.WorkerRental;

import io.restassured.common.mapper.TypeRef;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WorkerTest {

    @Autowired
    WorkerRepository workerRepository;

    private WorkerDTO createDefaultWorker(){
        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("Adam");
        return given().contentType("application/json").
                body(createWorkerDTO).when().post("/workers").
                then().statusCode(200).
                extract().as(WorkerDTO.class);
    }
    private WorkerDTO getWorker(UUID id){
        return get("/workers/{id}", id).as(WorkerDTO.class);
    }
    private WorkerDTO updateWorker(UUID id, CreateWorkerDTO createWorkerDTO){
        return given().contentType("application/json").
                body(createWorkerDTO).when().post("/workers/{id}", id).
                then().statusCode(200).
                extract().as(WorkerDTO.class);
    }
    private void deleteWorker(UUID id){
        delete("/workers/{id}", id);
    }

    @BeforeEach
    public void setup(){
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

        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("Marek");

        updateWorker(worker.getId(), createWorkerDTO);
        WorkerDTO worker2 = getWorker(worker.getId());
        assertEquals("Marek", worker2.getName());
    }

    @Test
    void deleteWorkerTest(){
        WorkerDTO worker = createDefaultWorker();
        deleteWorker(worker.getId());

        List<WorkerDTO> workers = get("/workers").as(new TypeRef<List<WorkerDTO>>() {});

        assertEquals(0, workers.size());
    }


}
