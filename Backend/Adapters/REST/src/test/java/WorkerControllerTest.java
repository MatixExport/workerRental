import entities.WorkerEnt;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import helper.RestModelFactory;
import helper.RestTestConfiguration;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import spring.controllers.WorkerController;
import spring.controllers.exception.handlers.GlobalExceptionHandler;
import view.WorkerService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;


@WebMvcTest(WorkerController.class)
@ContextConfiguration(classes = RestTestConfiguration.class)
class WorkerControllerTest {
    private final String baseUri = "/workers";

    @Mock
    private WorkerService workerService;

    @InjectMocks
    private WorkerController workerController;


    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone() {
        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(workerController).setControllerAdvice(GlobalExceptionHandler.class);
        RestAssuredMockMvc.standaloneSetup(builder);
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
    void getWorkersTest() {
        WorkerEnt workerEnt = RestModelFactory.getWorkerEnt();
        WorkerEnt workerEnt2 = RestModelFactory.getWorkerEnt();
        Mockito.when(workerService.findAll()).thenReturn(List.of(workerEnt, workerEnt2));

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(List.of(workerEnt.getId().toString(), workerEnt2.getId().toString())));
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

    @Test
    void deleteWorkerTest() throws WorkerRentedException, ResourceNotFoundException {
        Mockito.doNothing().when(workerService).delete(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .when()
                .delete(baseUri + "/{id}", UUID.randomUUID().toString()).
                then()
                .statusCode(HttpStatus.OK.value());
    }



}
