package indie.outsource.ApplicationCore;

import indie.outsource.ApplicationCore.repositories.UserRepository;
import indie.outsource.ApplicationCore.repositories.RentRepository;
import indie.outsource.ApplicationCore.repositories.WorkerRepository;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import io.restassured.common.mapper.TypeRef;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static indie.outsource.ApplicationCore.requests.UserRequests.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Disabled
class UserTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    RentRepository rentRepository;

    @Before
    public void fullTeardown(){
        rentRepository.deleteAll();
        userRepository.deleteAll();
        workerRepository.deleteAll();
    }

    @AfterEach
    public void teardown(){
        userRepository.deleteAll();
    }



    @Test
    void createUserTest(){
        UserDTO user = createDefaultUser();

        UserDTO user2 = getUser(user.getId());

        assertEquals("Adam", user2.getLogin());
    }

    @Test
    void readUserTest(){
        UserDTO user = createDefaultUser();

        UserDTO user2 = getUser(user.getId());

        assertEquals(user.getLogin(), user2.getLogin());

        UserDTO user3 = get("/users/login/{login}", user2.getLogin()).as(UserDTO.class);
        assertEquals(user.getLogin(), user3.getLogin());

        UserDTO user4 = get("/users/loginContains/{login}", "ADA").as(new TypeRef<List<UserDTO>>() {}).getFirst();
        assertEquals(user.getLogin(), user4.getLogin());

        UserDTO user5 = getUsers().getFirst();
        assertEquals(user.getLogin(), user5.getLogin());
    }

    @Test
    void getNonexistentUserTest(){
        assertEquals(404, get("/users/{id}", UUID.randomUUID()).statusCode());
    }

    @Test
    void updateUserTest(){
        UserDTO user = createDefaultUser();
        updateUser(user.getId(), new CreateUserDTO("Marek", "ZAQ!2wsx", USERTYPE.ADMIN));
        UserDTO user2 = getUser(user.getId());

        assertEquals("Marek", user2.getLogin());
    }

    @Test
    void activateUserTest(){
        UserDTO user = createDefaultUser();

        activateUser(user.getId());

        assertTrue(getUser(user.getId()).isActive());
    }

    @Test
    void deactivateUserTest(){
        UserDTO user = createDefaultUser();

        activateUser(user.getId());
        assertTrue(getUser(user.getId()).isActive());

        deactivateUser(user.getId());
        assertFalse(getUser(user.getId()).isActive());
    }

    @Test
    void tooShortUsernameTest(){
        CreateUserDTO createUserDTO = new CreateUserDTO("A", "ZAQ!2wsx", USERTYPE.CLIENT);
        given().contentType("application/json").
                body(createUserDTO).when().post("/users").
                then().statusCode(400);
        assertEquals(0, getUsers().size());
    }

    @Test
    void usernameExistsTest(){
        UserDTO user = createDefaultUser();
        CreateUserDTO createUserDTO = new CreateUserDTO("Adam", "ZAQ!2wsx", USERTYPE.CLIENT);
        given().contentType("application/json").
                body(createUserDTO).when().post("/users").
                then().statusCode(409);
        assertEquals(1, getUsers().size());
    }


}
