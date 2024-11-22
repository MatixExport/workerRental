package indie.outsource.WorkerRental;

import indie.outsource.WorkerRental.repositories.RentRepository;
import indie.outsource.WorkerRental.repositories.UserRepository;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import indie.outsource.WorkerRental.DTO.rent.CreateRentDTO;
import indie.outsource.WorkerRental.DTO.rent.RentDTO;
import indie.outsource.WorkerRental.DTO.user.UserDTO;
import indie.outsource.WorkerRental.DTO.worker.WorkerDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static indie.outsource.WorkerRental.requests.RentRequests.createDefaultRent;
import static indie.outsource.WorkerRental.requests.RentRequests.createRent;

import static indie.outsource.WorkerRental.requests.RentRequests.*;
import static indie.outsource.WorkerRental.requests.UserRequests.activateUser;
import static indie.outsource.WorkerRental.requests.UserRequests.createDefaultUser;
import static indie.outsource.WorkerRental.requests.WorkerRequests.createDefaultWorker;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class RentTest {

    @Inject
    UserRepository userRepository;
    @Inject
    WorkerRepository workerRepository;
    @Inject
    RentRepository rentRepository;

    @BeforeEach
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
