package indie.outsource.WorkerRental;

import indie.outsource.WorkerRental.rent.RentRepository;
import indie.outsource.WorkerRental.user.UserRepository;
import indie.outsource.WorkerRental.worker.WorkerRepository;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import indie.outsource.user.UserDTO;
import indie.outsource.worker.WorkerDTO;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static indie.outsource.WorkerRental.requests.RentRequests.createRent;

import static indie.outsource.WorkerRental.requests.UserRequests.activateUser;
import static indie.outsource.WorkerRental.requests.UserRequests.createDefaultUser;
import static indie.outsource.WorkerRental.requests.WorkerRequests.createDefaultWorker;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RentTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    RentRepository rentRepository;

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
        workerRepository.deleteAll();
        rentRepository.deleteAll();
    }


    @Test
    void rentTest(){
        UserDTO user = createDefaultUser();
        activateUser(user.getId());

        WorkerDTO worker = createDefaultWorker();

        CreateRentDTO createRentDTO = new CreateRentDTO(LocalDateTime.now().plusHours(2));
        RentDTO rent = createRent(user.getId(), worker.getId(), createRentDTO);

        assertEquals(1,get("/rents").as(new TypeRef<List<RentDTO>>() {}).size());
    }

    @Test
    void createRentForRentedWorkerTest(){
        UserDTO user = createDefaultUser();
        activateUser(user.getId());

        WorkerDTO worker = createDefaultWorker();

        CreateRentDTO createRentDTO = new CreateRentDTO(LocalDateTime.now().plusHours(2));
        RentDTO rent = createRent(user.getId(), worker.getId(), createRentDTO);

        CreateRentDTO createRentDTO2 = new CreateRentDTO(LocalDateTime.now().plusHours(20));
        given().contentType("application/json").
                body(createRentDTO2).when().post("/rents/users/{clientId}/workers/{workerId}", user.getId(), worker.getId()).
                then().statusCode(409);

        assertEquals(1,get("/rents").as(new TypeRef<List<RentDTO>>() {}).size());
    }
}
