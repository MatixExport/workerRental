package indie.outsource.ApplicationCore.dtoMappers;

import indie.outsource.ApplicationCore.model.user.Admin;
import indie.outsource.ApplicationCore.model.user.Client;
import indie.outsource.ApplicationCore.model.user.Manager;
import indie.outsource.ApplicationCore.model.user.User;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.SignedCreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;

public final class UserMapper {
    public static UserDTO getUserDTO(User user){
        return new UserDTO(user.getLogin(), user.isActive(), user.getId(), USERTYPE.getByClassname(user.getClass().getSimpleName()));
    }
    public static SignedCreateUserDTO getSignedUserDTO(User user){
        return new SignedCreateUserDTO(user.getLogin(),"" , USERTYPE.getByClassname(user.getClass().getSimpleName()));
    }

    public static CreateUserDTO getCreateUserDTOFromSigned(SignedCreateUserDTO signedUserDTO){
        return new CreateUserDTO(signedUserDTO.getLogin(),signedUserDTO.getPassword() , signedUserDTO.getType());
    }
    public static User getUser(CreateUserDTO createUserDTO){
        User user = new Client();
        if(createUserDTO.getType() == USERTYPE.CLIENT){
            user = new Client();
        }
        if(createUserDTO.getType() == USERTYPE.MANAGER){
            user = new Manager();
        }
        if(createUserDTO.getType() == USERTYPE.ADMIN){
            user = new Admin();
        }
        user.setLogin(createUserDTO.getLogin());
        user.setPassword(createUserDTO.getPassword());
        return user;
    }

}
