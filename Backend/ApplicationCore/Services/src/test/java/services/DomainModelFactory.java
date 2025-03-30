package services;

import entities.RentEnt;
import entities.WorkerEnt;
import entities.user.AdminEnt;
import entities.user.ClientEnt;
import entities.user.ManagerEnt;
import entities.user.UserEnt;
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
