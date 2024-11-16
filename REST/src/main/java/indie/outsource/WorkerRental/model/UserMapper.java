package indie.outsource.WorkerRental.model;

import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;

public final class UserMapper {
    public static UserDTO getUserDTO(User user){
        return new UserDTO(user.getLogin(), user.isActive(), user.getId());
    }
    public static User getUser(CreateUserDTO createUserDTO){
        User user = new User();
        user.setLogin(createUserDTO.getLogin());
        user.setPassword(createUserDTO.getPassword());
        return user;
    }
}
