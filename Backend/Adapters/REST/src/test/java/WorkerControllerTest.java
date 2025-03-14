import Entities.WorkerEnt;
import exceptions.ResourceNotFoundException;

import static org.hamcrest.Matchers.equalTo;


import exceptions.WorkerRentedException;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import io.restassured.http.ContentType;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spring.controllers.WorkerController;
import view.WorkerService;
import java.util.UUID;



@WebMvcTest(WorkerController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestTestConfiguration.class)
class WorkerControllerTest {
    private String baseUri = "/workers";

    @Mock
    private WorkerService workerService;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private WorkerController workerController;


    @BeforeEach
    public void setupSpringSecurity() {
        SecurityMockMvcConfigurers.springSecurity();
    }

    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone() {
        RestAssuredMockMvc.standaloneSetup(workerController);
    }

    @Test
    void getWorkerTest() throws ResourceNotFoundException {

        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        Mockito.when(workerService.findById(Mockito.any(UUID.class))).thenReturn(workerEnt);

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri+"/"+workerEnt.getId().toString())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(workerEnt.getId().toString()))
                .body("name", equalTo(workerEnt.getName()));
    }

    @Test
    void createWorkerTest() {
        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        Mockito.when(workerService.save(Mockito.any(WorkerEnt.class))).thenReturn(workerEnt);
        CreateWorkerDTO createWorkerDTO = RestModelFactory.getCreateWorkerDTO();

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createWorkerDTO)
                .post(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(workerEnt.getId().toString()))
                .body("name", equalTo(workerEnt.getName()));
    }

    @Test
    void updateWorkerTest() throws ResourceNotFoundException {
        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        Mockito.when(workerService.updateWorker(Mockito.any(WorkerEnt.class))).thenReturn(workerEnt);

        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("test1");

        WorkerDTO updatedWorker = RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createWorkerDTO)
                .when()
                .post(baseUri+"/{id}", workerEnt.getId().toString()).
                then()
                .statusCode(HttpStatus.OK.value()).
                extract()
                .as(WorkerDTO.class);

        Assertions.assertEquals(updatedWorker.getId(), workerEnt.getId());
        Assertions.assertEquals(updatedWorker.getName(), workerEnt.getName());
    }
    @Test
    void updateWorkerDoesNotExistTest() throws ResourceNotFoundException {
        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        Mockito.when(workerService.updateWorker(Mockito.any(WorkerEnt.class))).thenThrow(ResourceNotFoundException.class);

        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("test1");

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createWorkerDTO)
                .when()
                .post(baseUri+"/{id}", workerEnt.getId().toString()).
                then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
    @Test
    void deleteWorkerDoesNotExistTest() throws WorkerRentedException, ResourceNotFoundException {
        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        Mockito.doThrow(ResourceNotFoundException.class).when(workerService).delete(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .when()
                .delete(baseUri+"/{id}", workerEnt.getId().toString()).
                then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
    @Test
    void deleteWorkerIsRentedTest() throws WorkerRentedException, ResourceNotFoundException {
        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        Mockito.doThrow(WorkerRentedException.class).when(workerService).delete(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .when()
                .delete(baseUri+"/{id}", workerEnt.getId().toString()).
                then()
                .statusCode(HttpStatus.CONFLICT.value());
    }



}
