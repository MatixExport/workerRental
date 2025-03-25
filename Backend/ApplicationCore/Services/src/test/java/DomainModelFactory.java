import Entities.RentEnt;
import Entities.WorkerEnt;
import Entities.user.AdminEnt;
import Entities.user.ClientEnt;
import Entities.user.ManagerEnt;
import Entities.user.UserEnt;
import org.instancio.Instancio;

public class DomainModelFactory {
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
}
