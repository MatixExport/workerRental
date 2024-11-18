package indie.outsource.WorkerRental.dtoMappers;

import indie.outsource.WorkerRental.model.user.Admin;
import indie.outsource.WorkerRental.model.user.Client;
import indie.outsource.WorkerRental.model.user.Manager;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;

public final class UserMapper {
    public static UserDTO getUserDTO(User user){
        System.out.println(user.getClass().getSimpleName());
        return new UserDTO(user.getLogin(), user.isActive(), user.getId(), USERTYPE.getByClassname(user.getClass().getSimpleName()));
    }
    public static User getUser(CreateUserDTO createUserDTO){
        User user = new User();
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
