import Entities.RentEnt;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WorkerRentedException;
import helper.RestModelFactory;
import helper.RestTestConfiguration;
import indie.outsource.rent.CreateRentDTO;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import spring.controllers.RentController;
import view.RentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;



@ContextConfiguration(classes = RestTestConfiguration.class)
@AutoConfigureMockMvc()
class RentControllerTest {
    private final String baseUri = "/rents";

    @Mock
    private RentService rentService;

    @InjectMocks
    private RentController rentController;

    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone() {
        RestAssuredMockMvc.standaloneSetup(rentController);
    }

    @Test
    void getRentTest() throws ResourceNotFoundException {
        RentEnt rentEnt = RestModelFactory.getRentEnt();
        Mockito.when(rentService.findById(Mockito.any(UUID.class))).thenReturn(rentEnt);

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri+"/"+rentEnt.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(rentEnt.getId().toString()));
    }

    @Test
    void getRentWhenNotFoundTest() throws ResourceNotFoundException {
        Mockito.when(rentService.findById(Mockito.any(UUID.class))).thenThrow(new ResourceNotFoundException());
        RestAssuredMockMvc.given()
                .when()
                .get(baseUri + "/" + UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getAllRentsTest() {
        RentEnt rentEnt = RestModelFactory.getRentEnt();
        RentEnt rentEnt2 = RestModelFactory.getRentEnt();
        Mockito.when(rentService.findAll()).thenReturn(List.of(rentEnt, rentEnt2));

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(List.of(rentEnt.getId().toString(), rentEnt2.getId().toString())));
    }

    @Test
    void getAllRentsWhenEmptyTest() {
        Mockito.when(rentService.findAll()).thenReturn(List.of());

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(equalTo("[]"));
    }

    @Test
    void createRentTest() throws WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        RentEnt rentEnt = helper.RestModelFactory.getRentEnt();
        CreateRentDTO createRentDTO = helper.RestModelFactory.getCreateRentDTO();
        Mockito.when(rentService.createRent(
                Mockito.any(UUID.class), Mockito.any(UUID.class), Mockito.any(LocalDateTime.class)
        )).thenReturn(rentEnt);

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createRentDTO)
                .post(baseUri + "/users/{clientId}/workers/{workerId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(rentEnt.getId().toString()));
    }

    @Test
    void createRentWhenNotFoundTest() throws WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        CreateRentDTO createRentDTO = helper.RestModelFactory.getCreateRentDTO();
        Mockito.when(rentService.createRent(
                Mockito.any(UUID.class), Mockito.any(UUID.class), Mockito.any(LocalDateTime.class)
        )).thenThrow(new ResourceNotFoundException());

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createRentDTO)
                .post(baseUri + "/users/{clientId}/workers/{workerId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createRentWhenWorkerRentedTest() throws WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        CreateRentDTO createRentDTO = helper.RestModelFactory.getCreateRentDTO();
        Mockito.when(rentService.createRent(
                Mockito.any(UUID.class), Mockito.any(UUID.class), Mockito.any(LocalDateTime.class)
        )).thenThrow(new WorkerRentedException());

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createRentDTO)
                .post(baseUri + "/users/{clientId}/workers/{workerId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void createRentWhenUserInactive() throws WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        CreateRentDTO createRentDTO = helper.RestModelFactory.getCreateRentDTO();
        Mockito.when(rentService.createRent(
                Mockito.any(UUID.class), Mockito.any(UUID.class), Mockito.any(LocalDateTime.class)
        )).thenThrow(new UserInactiveException());

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(createRentDTO)
                .post(baseUri + "/users/{clientId}/workers/{workerId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void deleteRentTest() throws ResourceNotFoundException, RentAlreadyEndedException {
        Mockito.doNothing().when(rentService).deleteRent(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .delete(baseUri + "/{id}/delete", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteRentWhenNotFoundTest() throws ResourceNotFoundException, RentAlreadyEndedException {
        Mockito.doThrow(new ResourceNotFoundException()).when(rentService).deleteRent(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .delete(baseUri + "/{id}/delete", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deleteRentWhenFinishedTest() throws ResourceNotFoundException, RentAlreadyEndedException {
        Mockito.doThrow(new RentAlreadyEndedException()).when(rentService).deleteRent(Mockito.any(UUID.class));

        RestAssuredMockMvc
                .given()
                .delete(baseUri + "/{id}/delete", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void getClientEndedRentsTest() throws ResourceNotFoundException {
        RentEnt rentEnt = helper.RestModelFactory.getRentEnt();
        RentEnt rentEnt2 = helper.RestModelFactory.getRentEnt();

        Mockito.when(rentService.getClientEndedRents(Mockito.any(UUID.class))).thenReturn(List.of(rentEnt, rentEnt2));

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/ended/users/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(List.of(rentEnt.getId().toString(), rentEnt2.getId().toString())));
    }

    @Test
    void getClientEndedRentsWhenNotFoundTest() throws ResourceNotFoundException {
        Mockito.when(rentService.getClientEndedRents(Mockito.any(UUID.class))).thenThrow(new ResourceNotFoundException());

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/ended/users/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getClientCurrentRentsTest() throws ResourceNotFoundException {
        RentEnt rentEnt = helper.RestModelFactory.getRentEnt();
        RentEnt rentEnt2 = helper.RestModelFactory.getRentEnt();

        Mockito.when(rentService.getClientActiveRents(Mockito.any(UUID.class))).thenReturn(List.of(rentEnt, rentEnt2));

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/current/users/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(List.of(rentEnt.getId().toString(), rentEnt2.getId().toString())));
    }

    @Test
    void getClientCurrentRentsWhenNotFoundTest() throws ResourceNotFoundException {
        Mockito.when(rentService.getClientActiveRents(Mockito.any(UUID.class))).thenThrow(new ResourceNotFoundException());

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/current/users/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    @Test
    void getWorkerEndedRentsTest() throws ResourceNotFoundException {
        RentEnt rentEnt = helper.RestModelFactory.getRentEnt();
        RentEnt rentEnt2 = helper.RestModelFactory.getRentEnt();

        Mockito.when(rentService.getWorkerEndedRents(Mockito.any(UUID.class))).thenReturn(List.of(rentEnt, rentEnt2));

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/ended/workers/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(List.of(rentEnt.getId().toString(), rentEnt2.getId().toString())));
    }

    @Test
    void getWorkerEndedRentsWhenNotFoundTest() throws ResourceNotFoundException {
        Mockito.when(rentService.getWorkerEndedRents(Mockito.any(UUID.class))).thenThrow(new ResourceNotFoundException());

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/ended/workers/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getWorkerCurrentRentsTest() throws ResourceNotFoundException {
        RentEnt rentEnt = helper.RestModelFactory.getRentEnt();
        RentEnt rentEnt2 = helper.RestModelFactory.getRentEnt();

        Mockito.when(rentService.getWorkerActiveRents(Mockito.any(UUID.class))).thenReturn(List.of(rentEnt, rentEnt2));

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/current/workers/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(List.of(rentEnt.getId().toString(), rentEnt2.getId().toString())));
    }

    @Test
    void getWorkerCurrentRentsWhenNotFoundTest() throws ResourceNotFoundException {
        Mockito.when(rentService.getWorkerActiveRents(Mockito.any(UUID.class))).thenThrow(new ResourceNotFoundException());

        RestAssuredMockMvc
                .given()
                .get(baseUri + "/current/workers/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}

