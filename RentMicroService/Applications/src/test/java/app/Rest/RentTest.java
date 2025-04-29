package app.Rest;

import app.testcontainers.MongoTestContainer;
import entities.RentEnt;
import entities.WorkerEnt;
import entities.user.UserEnt;
import app.helper.RestModelFactory;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserInactiveException;
import exceptions.WorkerRentedException;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import infrastructure.RentRepository;
import infrastructure.UserRepository;
import infrastructure.WorkerRepository;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import view.RentService;
import view.UserService;
import view.WorkerService;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RentTest extends MongoTestContainer {

    @LocalServerPort
    private int port;

    private static final String BASE_URI = "https://localhost";
    private static final String BASE_RENT_URI = "/rents";

    @Autowired
    TestAuthHelper authHelper;

    private static RestAssuredConfig rac = RestAssured
            .config()
            .sslConfig(new SSLConfig()
                    .allowAllHostnames()
                    .relaxedHTTPSValidation("TLSv1.2"));

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    RentService rentService;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentRepository rentRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        workerRepository.deleteAll();
        rentRepository.deleteAll();
    }

    @BeforeEach
    void setupRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    void createRentTest() throws UserAlreadyExistsException, ResourceNotFoundException {
        UserEnt user = RestModelFactory.getClientEnt();
        userService.save(user);
        String jwtToken = authHelper.generateJWT(user, 2000, "CLIENT");
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());


        CreateRentDTO dto = new CreateRentDTO();
        dto.setStartDate(LocalDateTime.now().plusMinutes(10));

        RentDTO rentDTO =  RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(dto)
                .post(BASE_RENT_URI +"/user/workers/"+worker.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(RentDTO.class);


        Assertions.assertEquals(worker.getId(), rentDTO.getWorkerID());
        Assertions.assertEquals(user.getId(), rentDTO.getUserID());
        Assertions.assertEquals(rentService.findById(rentDTO.getId()).getEndDate(), rentDTO.getEndDate());
    }
    @Test
    void createRentWorkerAlreadyRentedTest() throws UserAlreadyExistsException, WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        UserEnt user = RestModelFactory.getClientEnt();
        userService.save(user);
        String jwtToken = authHelper.generateJWT(user, 2000, "CLIENT");
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());
        rentService.createRent(user.getId(),worker.getId(), LocalDateTime.now());

        CreateRentDTO dto = new CreateRentDTO();
        dto.setStartDate(LocalDateTime.now().plusMinutes(10));

        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(dto)
                .post(BASE_RENT_URI +"/user/workers/"+worker.getId())
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }


    @Test
    void deleteRentTest() throws UserAlreadyExistsException, WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        UserEnt user = RestModelFactory.getAdminEnt();
        userService.save(user);
        String jwtToken = authHelper.generateJWT(user, 2000, "ADMIN");
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());
        RentEnt rentEnt =  rentService.createRent(user.getId(),worker.getId(), LocalDateTime.now().plusMinutes(10));


        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .delete(BASE_RENT_URI +"/"+rentEnt.getId()+"/delete")
                .then()
                .statusCode(HttpStatus.OK.value());


        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            rentService.findById(rentEnt.getId());
        });

    }

}
