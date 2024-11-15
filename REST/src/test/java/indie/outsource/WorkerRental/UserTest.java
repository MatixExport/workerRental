package indie.outsource.WorkerRental;

import indie.outsource.WorkerRental.user.UserRepository;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    private UserDTO createDefaultUser(){
        CreateUserDTO createUserDTO = new CreateUserDTO("Adam", "ZAQ!2wsx");
        return given().contentType("application/json").
                body(createUserDTO).when().post("/users").
                then().statusCode(200).
                extract().as(UserDTO.class);
    }
    private UserDTO getUser(UUID id){
        return get("/users/{id}", id).as(UserDTO.class);
    }
    private UserDTO updateUser(UUID id, CreateUserDTO createUserDTO){
        return given().contentType("application/json").
                body(createUserDTO).when().post("/users/{id}", id).
                then().statusCode(200).
                extract().as(UserDTO.class);
    }

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void createUserTest(){
        UserDTO user = createDefaultUser();

        assertEquals("Adam", user.getLogin());

        UserDTO user2 = getUser(user.getId());

        assertEquals(user.getLogin(), user2.getLogin());
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

        UserDTO user5 = get("/users").as(new TypeRef<List<UserDTO>>() {}).getFirst();
        assertEquals(user.getLogin(), user5.getLogin());
    }

    @Test
    void updateUserTest(){
        UserDTO user = createDefaultUser();

        CreateUserDTO createUserDTO = new CreateUserDTO("Marek", "ZAQ!2wsx");

        updateUser(user.getId(), createUserDTO);
        UserDTO user2 = getUser(user.getId());
        assertEquals("Marek", user2.getLogin());
    }

    @Test
    void activateUserTest(){
        UserDTO user = createDefaultUser();

        post("/users/{id}/activate", user.getId()).then().statusCode(200);

        assertTrue(getUser(user.getId()).isActive());
    }

    @Test
    void deactivateUserTest(){
        UserDTO user = createDefaultUser();

        post("/users/{id}/activate", user.getId()).then().statusCode(200);
        assertTrue(getUser(user.getId()).isActive());

        post("/users/{id}/deactivate", user.getId()).then().statusCode(200);
        assertFalse(getUser(user.getId()).isActive());
    }


}
