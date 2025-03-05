package indie.outsource.ApplicationCore;

import indie.outsource.ApplicationCore.repositories.RentRepository;
import indie.outsource.ApplicationCore.repositories.UserRepository;
import indie.outsource.ApplicationCore.repositories.WorkerRepository;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import indie.outsource.user.UserDTO;
import indie.outsource.worker.WorkerDTO;
import io.restassured.common.mapper.TypeRef;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static indie.outsource.ApplicationCore.requests.RentRequests.createDefaultRent;
import static indie.outsource.ApplicationCore.requests.RentRequests.createRent;

import static indie.outsource.ApplicationCore.requests.RentRequests.*;
import static indie.outsource.ApplicationCore.requests.UserRequests.activateUser;
import static indie.outsource.ApplicationCore.requests.UserRequests.createDefaultUser;
import static indie.outsource.ApplicationCore.requests.WorkerRequests.createDefaultWorker;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Disabled
class RentTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    RentRepository rentRepository;

    @Before
    @AfterEach
    public void teardown(){
        rentRepository.deleteAll();
        userRepository.deleteAll();
        workerRepository.deleteAll();
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

        assertEquals(0,get("/rents").as(new TypeRef<List<RentDTO>>() {}).size());

        CreateRentDTO createRentDTO = new CreateRentDTO(LocalDateTime.now().plusHours(2));
        RentDTO rent = createRent(user.getId(), worker.getId(), createRentDTO);

        CreateRentDTO createRentDTO2 = new CreateRentDTO(LocalDateTime.now().plusHours(20));
        given().contentType("application/json").
                body(createRentDTO2).when().post("/rents/users/{clientId}/workers/{workerId}", user.getId(), worker.getId()).
                then().statusCode(409);
        assertEquals(1,get("/rents").as(new TypeRef<List<RentDTO>>() {}).size());
    }

    @Test
    void deleteRentTest(){
        RentDTO rentDTO = createDefaultRent();

        delete("/rents/{rentId}/delete", rentDTO.getId()).then().statusCode(200);
        assertEquals(0,get("/rents").as(new TypeRef<List<RentDTO>>() {}).size());
    }

    @Test
    void getRentsTest(){
        RentDTO rentDTO = createDefaultRent();

        assertEquals(1,get("/rents/current/users/{id}",rentDTO.getUserID()).then().statusCode(200).extract().as(new TypeRef<List<RentDTO>>() {}).size());
        assertEquals(1,get("/rents/current/workers/{id}",rentDTO.getWorkerID()).as(new TypeRef<List<RentDTO>>() {}).size());
        assertEquals(0,get("/rents/ended/users/{id}",rentDTO.getUserID()).as(new TypeRef<List<RentDTO>>() {}).size());
        assertEquals(0,get("/rents/ended/workers/{id}",rentDTO.getWorkerID()).as(new TypeRef<List<RentDTO>>() {}).size());

        finishRent(rentDTO.getId(),200);

        assertEquals(1,get("/rents/ended/users/{id}",rentDTO.getUserID()).as(new TypeRef<List<RentDTO>>() {}).size());
        assertEquals(1,get("/rents/ended/workers/{id}",rentDTO.getWorkerID()).as(new TypeRef<List<RentDTO>>() {}).size());
        assertEquals(0,get("/rents/current/users/{id}",rentDTO.getUserID()).then().statusCode(200).extract().as(new TypeRef<List<RentDTO>>() {}).size());
        assertEquals(0,get("/rents/current/workers/{id}",rentDTO.getWorkerID()).as(new TypeRef<List<RentDTO>>() {}).size());

    }


    @Test
    void finishRentTest(){
        RentDTO rent1 =  createDefaultRent();
        finishRent(rent1.getId(),200);
        rent1 = getRent(rent1.getId());
        assertNotNull(rent1.getEndDate());
        finishRent(rent1.getId(),409);
    }

}
