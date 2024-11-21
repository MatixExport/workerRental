package indie.outsource.WorkerRental.requests;

import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import indie.outsource.user.UserDTO;
import io.restassured.common.mapper.TypeRef;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.*;

public class RentRequests {
    public static RentDTO createRent(UUID userId, UUID workerId, CreateRentDTO createRentDTO){
        return given().contentType("application/json").
                body(createRentDTO).when().post("/rents/users/{clientId}/workers/{workerId}", userId, workerId).
                then().statusCode(200).
                extract().as(RentDTO.class);
    }
    public static void finishRent(UUID id,Integer expectedCode){
        post("/rents/{id}/finish", id).then().statusCode(expectedCode);
    }

    public static RentDTO getRent(UUID id){
        return get("/rents/{id}", id).as(RentDTO.class);
    }




}
