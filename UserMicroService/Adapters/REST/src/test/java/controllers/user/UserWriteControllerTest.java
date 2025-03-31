package controllers.user;

import entities.user.UserEnt;
import exceptions.UserAlreadyExistsException;
import helper.RestModelFactory;
import indie.outsource.user.CreateUserDTO;
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
import spring.controllers.user.UserWriteController;
import view.UserService;

import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(UserWriteController.class)
@ContextConfiguration(classes = helper.RestTestConfiguration.class)
class UserWriteControllerTest {
    private final String baseUri = "/users";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserWriteController userWriteController;

    @BeforeEach
    void initialiseRestAssuredMockMvcStandalone() {
        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(userWriteController).setControllerAdvice(new GlobalExceptionHandler());
        RestAssuredMockMvc.standaloneSetup(builder);
    }

    @Test
    void createUserTest() throws UserAlreadyExistsException {
        UserEnt userEnt = RestModelFactory.getUserEnt();
        Mockito.when(userService.save(Mockito.any(UserEnt.class))).thenReturn(userEnt);
        CreateUserDTO createUserDTO = RestModelFactory.getCreateUserDTO();

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(createUserDTO)
                .post(baseUri)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(userEnt.getId().toString()));
    }

    @Test
    void createUserAlreadyExistsTest() throws UserAlreadyExistsException {
        Mockito.when(userService.save(Mockito.any(UserEnt.class))).thenThrow(UserAlreadyExistsException.class);
        CreateUserDTO createUserDTO = RestModelFactory.getCreateUserDTO();

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(createUserDTO)
                .post(baseUri)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .contentType(ContentType.JSON)
                .body("login", equalTo(createUserDTO.getLogin()));
    }


}
