package app;

import Entities.user.UserEnt;
import app.helper.RestModelFactory;
import app.helper.RestRequests;
import exceptions.UserAlreadyExistsException;
import infrastructure.UserRepository;
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
import view.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class AuthTest {

    @LocalServerPort
    private int port;


    private static final String baseUri = "https://localhost";
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

    @Autowired private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
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
    void jwtAuthTest() throws UserAlreadyExistsException {
        UserEnt user = RestModelFactory.getClientEnt();
        String password = user.getPassword();
        user.setActive(true);
        userService.save(user);

        String jwtToken = restRequests.getValidJwtForUser(
                user.getLogin(),password
        );

        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .get("/workers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();
    }
    @Test
    void noJwtAuthTest() throws UserAlreadyExistsException {
        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .get("/workers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void invalidJwtAuthTest() throws UserAlreadyExistsException {
        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + "CompletlyInvalidJwtToken")
                .get("/workers")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract().body().asString();
    }

    @Test
    void expiredJwtAuthTest() throws UserAlreadyExistsException {
        UserEnt user = RestModelFactory.getClientEnt();
        user.setActive(true);
        userService.save(user);

        String jwtToken = authHelper.generateJWT(user,0);

        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .get("/workers")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract().body().asString();

    }






}
