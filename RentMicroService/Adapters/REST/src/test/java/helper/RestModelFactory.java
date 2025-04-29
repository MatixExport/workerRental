package helper;

import entities.RentEnt;
import entities.WorkerEnt;
import entities.user.UserEnt;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.FinishRentDTO;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.worker.CreateWorkerDTO;
import org.instancio.Instancio;
import org.instancio.Select;

public class RestModelFactory {
    public static WorkerEnt getWorkerEnt(){
        return Instancio.of(WorkerEnt.class)
                .create();
    }
    public static RentEnt getRentEnt(){
        RentEnt rent =  Instancio.of(RentEnt.class)
                .create();
        rent.setUser(getClientEnt());
        rent.setWorker(getWorkerEnt());
        return rent;
    }
    public static UserEnt getAdminEnt(){
        return Instancio.of(UserEnt.class)
                .create();
    }
    public static UserEnt getClientEnt(){
        return Instancio.of(UserEnt.class)
                .create();
    }
    public static UserEnt getManagerEnt(){
        return Instancio.of(UserEnt.class)
                .create();
    }
    public static CreateWorkerDTO getCreateWorkerDTO(){
        return Instancio.of(CreateWorkerDTO.class)
                .create();
    }
    public static CreateUserDTO getCreateUserDTO(){
        return Instancio.of(CreateUserDTO.class)
                .create();
    }
    public static FinishRentDTO getFinishRentDTO(){
        return Instancio.of(FinishRentDTO.class)
                .create();
    }
    public static CreateRentDTO getCreateRentDTO(){
        return Instancio.of(CreateRentDTO.class)
                .generate(
                        Select.field(CreateRentDTO::getStartDate),
                        generators -> generators.temporal().localDateTime().future()
                ).create();
    }
}
