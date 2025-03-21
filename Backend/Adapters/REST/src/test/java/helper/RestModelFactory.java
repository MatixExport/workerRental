package helper;

import Entities.RentEnt;
import Entities.WorkerEnt;
import Entities.user.AdminEnt;
import Entities.user.ClientEnt;
import Entities.user.ManagerEnt;
import Entities.user.UserEnt;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.FinishRentDTO;
import indie.outsource.user.ChangePasswordDto;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.worker.CreateWorkerDTO;
import org.instancio.Instancio;

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
        return Instancio.of(AdminEnt.class)
                .create();
    }
    public static UserEnt getClientEnt(){
        return Instancio.of(ClientEnt.class)
                .create();
    }
    public static UserEnt getManagerEnt(){
        return Instancio.of(ManagerEnt.class)
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
    public static ChangePasswordDto getChangePasswordDTO(){
        return Instancio.of(ChangePasswordDto.class)
                .create();
    }
    public static FinishRentDTO getFinishRentDTO(){
        return Instancio.of(FinishRentDTO.class)
                .create();
    }
    public static CreateRentDTO getCreateRentDTO(){
        return Instancio.of(CreateRentDTO.class)
                .create();
    }
}
