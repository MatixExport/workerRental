package requests;

import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import io.restassured.common.mapper.TypeRef;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.*;

public class WorkerRequests {
    public static WorkerDTO createDefaultWorker(){
        CreateWorkerDTO createWorkerDTO = new CreateWorkerDTO("Adam");
        return given().contentType("application/json").
                body(createWorkerDTO).when().post("/workers").
                then().statusCode(HttpStatus.OK.value()).
                extract().as(WorkerDTO.class);
    }
    public static WorkerDTO getWorker(UUID id){
        return get("/workers/{id}", id).as(WorkerDTO.class);
    }
    public static WorkerDTO updateWorker(UUID id, CreateWorkerDTO createWorkerDTO){
        return given().contentType("application/json").
                body(createWorkerDTO).when().post("/workers/{id}", id).
                then().statusCode(200).
                extract().as(WorkerDTO.class);
    }
    public static void deleteWorker(UUID id){
        delete("/workers/{id}", id);
    }
    public static List<WorkerDTO> getWorkers(){
        return get("/workers").as(new TypeRef<List<WorkerDTO>>() {});
    }
}
