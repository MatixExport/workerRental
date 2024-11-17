package indie.outsource.WorkerRental.requests;

import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class RentRequests {
    public static RentDTO createRent(UUID userId, UUID workerId, CreateRentDTO createRentDTO){
        return given().contentType("application/json").
                body(createRentDTO).when().post("/rents/users/{clientId}/workers/{workerId}", userId, workerId).
                then().statusCode(200).
                extract().as(RentDTO.class);
    }

}
