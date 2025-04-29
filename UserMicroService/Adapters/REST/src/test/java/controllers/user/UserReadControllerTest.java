package controllers.user;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import helper.RestModelFactory;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
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
import spring.controllers.exception.handlers.GlobalExceptionHandler;
import spring.controllers.user.UserReadController;
import view.UserService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(UserReadController.class)
@ContextConfiguration(classes = helper.RestTestConfiguration.class)
class UserReadControllerTest {
    private final String baseUri = "/users";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserReadController userReadController;

    @BeforeEach
    void initialiseRestAssuredMockMvcStandalone() {
        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(userReadController).setControllerAdvice(new GlobalExceptionHandler());
        RestAssuredMockMvc.standaloneSetup(builder);
    }

    @Test
    void getAllUserWhenEmptyTest() {
        Mockito.when(userService.findAll()).thenReturn(List.<UserEnt>of());

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(equalTo("[]"));
    }

    @Test
    void getAllUsersTest() {
        UserEnt userEnt = RestModelFactory.getClientEnt();
        UserEnt userEnt2 = RestModelFactory.getClientEnt();
        Mockito.when(userService.findAll()).thenReturn(List.of(userEnt, userEnt2));

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(List.of(userEnt.getId().toString(), userEnt2.getId().toString())));
    }

    @Test
    void getUserTest() throws ResourceNotFoundException {
        UserEnt userEnt = RestModelFactory.getClientEnt();
        Mockito.when(userService.findById(Mockito.any(UUID.class))).thenReturn(userEnt);

        RestAssuredMockMvc.given()
                .when()
                .get(baseUri + "/" + userEnt.getId().toString())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(userEnt.getId().toString()));
    }

    @Test
    void getUserWhenNotFoundTest() throws ResourceNotFoundException {
        Mockito.when(userService.findById(Mockito.any(UUID.class))).thenThrow(ResourceNotFoundException.class);
        RestAssuredMockMvc.given()
                .when()
                .get(baseUri + "/" + UUID.randomUUID().toString())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
