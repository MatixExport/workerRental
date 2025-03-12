import Entities.RentEnt;
import Entities.WorkerEnt;
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
        return Instancio.of(RentEnt.class)
                .create();
    }
    public static UserEnt getUserEnt(){
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
