package services;

import entities.RentEnt;
import entities.WorkerEnt;
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
}
