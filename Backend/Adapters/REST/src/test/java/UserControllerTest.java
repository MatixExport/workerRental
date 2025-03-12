import Entities.WorkerEnt;
import exceptions.ResourceNotFoundException;

import static org.hamcrest.Matchers.equalTo;


import exceptions.WorkerRentedException;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import io.restassured.http.ContentType;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spring.controllers.UserController;
import spring.controllers.WorkerController;
import view.UserService;
import view.WorkerService;
import java.util.UUID;



@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestTestConfiguration.class)
@WithMockUser(username="admin",roles={"ADMIN"})
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



}

