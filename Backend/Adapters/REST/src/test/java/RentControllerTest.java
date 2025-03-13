import Entities.RentEnt;
import exceptions.ResourceNotFoundException;

import static org.hamcrest.Matchers.equalTo;


import io.restassured.http.ContentType;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.controllers.RentController;
import view.RentService;
import view.UserService;

import java.util.UUID;


//@WebMvcTest(RentController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestTestConfiguration.class)
@AutoConfigureMockMvc(webClientEnabled = true,webDriverEnabled = true)
@WithMockUser(username="admin",roles={"ADMIN"})
class RentControllerTest {
    private String baseUri = "/rents";

    @Mock
    private UserService userService;

    @Mock
    private RentService rentService;

    @Mock
    private Authentication authentication;

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

//
//    @Test
//    void createRentTest() throws UserAlreadyExistsException, WorkerRentedException, UserInactiveException, ResourceNotFoundException {
//        RentEnt rentEnt = RestModelFactory.getRentEnt();
//        CreateRentDTO createRentDTO = RestModelFactory.getCreateRentDTO();
//        Mockito.when(authentication.getDetails()).thenReturn("test");
//        Mockito.when(userService.findByUsername(Mockito.any(String.class)))
//                .thenReturn(List.of(RestModelFactory.getClientEnt()));
//        Mockito.when(rentService.createRent(
//                Mockito.any(),Mockito.any(),Mockito.any()
//        )).thenReturn(rentEnt);
//
//        RestAssuredMockMvc
//                .given()
//                .contentType("application/json")
//                .body(createRentDTO)
//                .post(baseUri+"/user/workers/"+rentEnt.getWorker().getId().toString())
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .contentType(ContentType.JSON)
//                .body("id", equalTo(rentEnt.getId().toString()));
//    }

//    @Test
//    void createUserAlreadyExistsTest() throws UserAlreadyExistsException {
//        Mockito.when(userService.save(Mockito.any(UserEnt.class))).thenThrow(UserAlreadyExistsException.class);
//        CreateUserDTO createUserDTO = RestModelFactory.getCreateUserDTO();
//
//        RestAssuredMockMvc
//                .given()
//                .contentType("application/json")
//                .body(createUserDTO)
//                .post(baseUri)
//                .then()
//                .statusCode(HttpStatus.CONFLICT.value())
//                .contentType(ContentType.JSON)
//                .body("login", equalTo(createUserDTO.getLogin()));
//    }

}

