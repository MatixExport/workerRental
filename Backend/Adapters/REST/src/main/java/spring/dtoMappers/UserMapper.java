package spring.dtoMappers;

import Entities.user.AdminEnt;
import Entities.user.ClientEnt;
import Entities.user.ManagerEnt;
import Entities.user.UserEnt;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.SignedCreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;

public final class UserMapper {
    public static UserDTO getUserDTO(UserEnt user){
        return new UserDTO(user.getLogin(), user.isActive(), user.getId(), USERTYPE.getByClassname(user.getClass().getSimpleName()));
    }
    public static SignedCreateUserDTO getSignedUserDTO(UserEnt user){
        return new SignedCreateUserDTO(user.getLogin(),"" , USERTYPE.getByClassname(user.getClass().getSimpleName()));
    }

    public static CreateUserDTO getCreateUserDTOFromSigned(SignedCreateUserDTO signedUserDTO){
        return new CreateUserDTO(signedUserDTO.getLogin(),signedUserDTO.getPassword() , signedUserDTO.getType());
    }
    public static UserEnt getUser(CreateUserDTO createUserDTO){
        UserEnt user = new ClientEnt();
        if(createUserDTO.getType() == USERTYPE.CLIENT){
            user = new ClientEnt();
        }
        if(createUserDTO.getType() == USERTYPE.MANAGER){
            user = new ManagerEnt();
        }
        if(createUserDTO.getType() == USERTYPE.ADMIN){
            user = new AdminEnt();
        }
        user.setLogin(createUserDTO.getLogin());
        user.setPassword(createUserDTO.getPassword());
        return user;
    }

}
