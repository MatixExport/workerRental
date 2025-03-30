package app;

import entities.RentEnt;
import entities.WorkerEnt;
import entities.user.UserEnt;
import app.helper.RestModelFactory;
import app.helper.RestRequests;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spring.security.AuthHelper;
import view.RentService;
import view.UserService;
import view.WorkerService;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class RentTest {

    @LocalServerPort
    private int port;


    private static final String baseUri = "https://localhost";
    private static final String baseRentUri = "/rents";

    private static RestRequests restRequests;


    @Autowired
    AuthHelper authHelper;

    private static RestAssuredConfig rac = RestAssured
            .config()
            .sslConfig(new SSLConfig()
                    .allowAllHostnames()
                    .relaxedHTTPSValidation("TLSv1.2"));

    @Container
    private final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.1");

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
    public void tearDown() {
        userRepository.deleteAll();
        workerRepository.deleteAll();
        rentRepository.deleteAll();
    }

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @BeforeEach
    void setupRestAssured() {
        RestAssured.baseURI = baseUri;
        RestAssured.port = port;
        restRequests = new RestRequests(port,baseUri,rac);
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Test
    void createRentTest() throws UserAlreadyExistsException, ResourceNotFoundException {
        UserEnt user = RestModelFactory.getClientEnt();
        user.setActive(true);
        userService.save(user);
        String jwtToken = authHelper.generateJWT(user);
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());


        CreateRentDTO dto = new CreateRentDTO();
        dto.setStartDate(LocalDateTime.now().plusMinutes(10));

        RentDTO rentDTO =  RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(dto)
                .post(baseRentUri+"/user/workers/"+worker.getId())
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
        userService.activateUser(user.getId());
        String jwtToken = authHelper.generateJWT(user);
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());
        rentService.createRent(user.getId(),worker.getId(), LocalDateTime.now());

        CreateRentDTO dto = new CreateRentDTO();
        dto.setStartDate(LocalDateTime.now().plusMinutes(10));

        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(dto)
                .post(baseRentUri+"/user/workers/"+worker.getId())
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void createRentInactiveUserTest() throws UserAlreadyExistsException, WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        UserEnt user = RestModelFactory.getClientEnt();
        userService.save(user);
        userService.deactivateUser(user.getId());
        String jwtToken = authHelper.generateJWT(user);
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());

        CreateRentDTO dto = new CreateRentDTO();
        dto.setStartDate(LocalDateTime.now().plusMinutes(10));

        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(dto)
                .post(baseRentUri+"/user/workers/"+worker.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void deleteRentTest() throws UserAlreadyExistsException, WorkerRentedException, UserInactiveException, ResourceNotFoundException {
        UserEnt user = RestModelFactory.getAdminEnt();
        userService.save(user);
        userService.activateUser(user.getId());
        String jwtToken = authHelper.generateJWT(user);
        WorkerEnt worker = workerService.save(RestModelFactory.getWorkerEnt());
        RentEnt rentEnt =  rentService.createRent(user.getId(),worker.getId(), LocalDateTime.now().plusMinutes(10));


        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .delete(baseRentUri+"/"+rentEnt.getId()+"/delete")
                .then()
                .statusCode(HttpStatus.OK.value());


        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            rentService.findById(rentEnt.getId());
        });

    }

}
