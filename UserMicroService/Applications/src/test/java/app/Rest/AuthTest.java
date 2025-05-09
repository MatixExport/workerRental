package app.Rest;

import app.testcontainers.MongoTestContainer;
import entities.user.UserEnt;
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
import spring.security.AuthHelper;
import view.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthTest extends MongoTestContainer {

    @LocalServerPort
    private int port;


    private static final String BASE_URI = "https://localhost";
    private static RestRequests restRequests;


    @Autowired
    AuthHelper authHelper;

    private static final RestAssuredConfig rac = RestAssured
            .config()
            .sslConfig(new SSLConfig()
                    .allowAllHostnames()
                    .relaxedHTTPSValidation("TLSv1.2"));

    @Autowired
    private UserService userService;

    @Autowired private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setupRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        restRequests = new RestRequests(port, BASE_URI,rac);
    }


    @Test
    void jwtAuthTest() throws UserAlreadyExistsException {
        UserEnt user = RestModelFactory.getAdminEnt();
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
                .get("/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();
    }
    @Test
    void noJwtAuthTest() {
        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .get("/users")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void invalidJwtAuthTest() {
        RestAssured.given()
                .config(rac)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + "CompletlyInvalidJwtToken")
                .get("/users")
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
                .get("/users")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract().body().asString();

    }

}
