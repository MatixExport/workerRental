package app.helper;

import entities.user.AdminEnt;
import entities.user.ClientEnt;
import entities.user.ManagerEnt;
import entities.user.UserEnt;
import indie.outsource.user.ChangePasswordDto;
import indie.outsource.user.CreateUserDTO;
import org.instancio.Instancio;

public class RestModelFactory {

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

    public static CreateUserDTO getCreateUserDTO(){
        return Instancio.of(CreateUserDTO.class)
                .create();
    }
    public static ChangePasswordDto getChangePasswordDTO(){
        return Instancio.of(ChangePasswordDto.class)
                .create();
    }

}
