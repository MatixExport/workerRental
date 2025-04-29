package services;

import entities.user.AdminEnt;
import entities.user.ClientEnt;
import entities.user.ManagerEnt;
import entities.user.UserEnt;
import org.instancio.Instancio;

public class DomainModelFactory {

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
