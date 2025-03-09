package indie.outsource.WorkerRental.requests;

import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import io.restassured.common.mapper.TypeRef;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.*;

public class UserRequests {
    public static UserDTO createDefaultUser(){
        CreateUserDTO createUserDTO = new CreateUserDTO("Adam", "ZAQ!2wsx", USERTYPE.CLIENT);
        return given().contentType("application/json").
                body(createUserDTO).when().post("/users").
                then().statusCode(200).
                extract().as(UserDTO.class);
    }
    public static UserDTO getUser(UUID id){
        return get("/users/{id}", id).as(UserDTO.class);
    }
    public static UserDTO updateUser(UUID id, CreateUserDTO createUserDTO){
        return given().contentType("application/json").
                body(createUserDTO).when().post("/users/{id}", id).
                then().statusCode(200).
                extract().as(UserDTO.class);
    }
    public static void activateUser(UUID id){
        post("/users/{id}/activate", id).then().statusCode(200);
    }
    public static void deactivateUser(UUID id){
        post("/users/{id}/deactivate", id).then().statusCode(200);
    }
    public static List<UserDTO> getUsers(){
        return get("/users").as(new TypeRef<List<UserDTO>>() {});
    }
}
