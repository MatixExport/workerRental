package services;

import entities.user.AdminEnt;
import entities.user.UserEnt;
import org.instancio.Instancio;

public class DomainModelFactory {

    public static UserEnt getAdminEnt(){
        return Instancio.of(AdminEnt.class)
                .create();
    }
    public static UserEnt getUserEnt(){
        return Instancio.of(UserEnt.class)
                .create();
    }

}
