package indie.outsource.WorkerRental.user;

import indie.outsource.WorkerRental.rent.Rent;
import indie.outsource.rent.RentDTO;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;

public final class UserMapper {
    static UserDTO getUserDTO(User user){
        return new UserDTO(user.getLogin(), user.isActive(), user.getId());
    }
    static User getUser(CreateUserDTO createUserDTO){
        User user = new User();
        user.setLogin(createUserDTO.getLogin());
        user.setPassword(createUserDTO.getPassword());
        return user;
    }
}
