import Entities.user.UserEnt;
import exceptions.ResourceNotFoundException;

import static org.hamcrest.Matchers.equalTo;


import exceptions.UserAlreadyExistsException;
import indie.outsource.user.CreateUserDTO;
import io.restassured.http.ContentType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.controllers.UserController;
import view.UserService;

import java.util.List;
import java.util.UUID;



@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestTestConfiguration.class)
@AutoConfigureMockMvc
class UserControllerTest {
    private String baseUri = "/users";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userControllerr;

    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone() {
        RestAssuredMockMvc.standaloneSetup(userControllerr);
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
                .get(baseUri+"/"+userEnt.getId().toString())
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
                .get(baseUri+"/"+UUID.randomUUID().toString())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createUserTest() throws UserAlreadyExistsException {
        UserEnt userEnt = RestModelFactory.getClientEnt();
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

