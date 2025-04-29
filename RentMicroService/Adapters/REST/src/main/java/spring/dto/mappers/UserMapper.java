package spring.dto.mappers;

import entities.user.UserEnt;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;

public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserDTO getUserDTO(UserEnt user){
        return new UserDTO(user.getLogin(), user.getId());
    }

    public static UserEnt getUser(CreateUserDTO createUserDTO){
        return new UserEnt(createUserDTO.getLogin());
    }

}
