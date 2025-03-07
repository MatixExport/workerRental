//import indie.outsource.ApplicationCore.repositories.WorkerRepository;
//import indie.outsource.ApplicationCore.repositories.RentRepository;
//import indie.outsource.ApplicationCore.repositories.UserRepository;
//import indie.outsource.worker.CreateWorkerDTO;
//import indie.outsource.worker.WorkerDTO;
//import org.junit.Before;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//import static indie.outsource.ApplicationCore.requests.WorkerRequests.*;
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ActiveProfiles("test")
//@Disabled
//class WorkerTest {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    WorkerRepository workerRepository;
//    @Autowired
//    RentRepository rentRepository;
//
//    @Before
//    public void fullTeardown(){
//        rentRepository.deleteAll();
//        userRepository.deleteAll();
//        workerRepository.deleteAll();
//    }
//
//    @AfterEach
//    public void teardown(){
//        workerRepository.deleteAll();
//    }
//
//    @Test
//    void createWorkerTest(){
//        WorkerDTO worker = createDefaultWorker();
//
//        assertEquals("Adam", worker.getName());
//
//        WorkerDTO worker2 = getWorker(worker.getId());
//
//        assertEquals(worker.getName(), worker2.getName());
//    }
//
//    @Test
//    void readWorkerTest(){
//        WorkerDTO worker = createDefaultWorker();
//
//        WorkerDTO worker2 = getWorker(worker.getId());
//
//        assertEquals(worker.getName(), worker2.getName());
//    }
//
//    @Test
//    void updateWorkerTest(){
//        WorkerDTO worker = createDefaultWorker();
//
//        updateWorker(worker.getId(), new CreateWorkerDTO("Marek"));
//        WorkerDTO worker2 = getWorker(worker.getId());
//        assertEquals("Marek", worker2.getName());
//    }
//
//    @Test
//    void deleteWorkerTest(){
//        WorkerDTO worker = createDefaultWorker();
//        deleteWorker(worker.getId());
//
//        List<WorkerDTO> workers = getWorkers();
//
//        assertEquals(0, workers.size());
//    }
//
//    @Test
//    void tooShortWorkerNameTest(){
//        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("A");
//        given().contentType("application/json").
//                body(createWorkerDTO).when().post("/workers").
//                then().statusCode(400);
//        assertEquals(0, getWorkers().size());
//    }
//
//
//}
