package aggregates.mappers;

import Entities.user.AdminEnt;
import Entities.user.ClientEnt;
import Entities.user.ManagerEnt;
import Entities.user.UserEnt;
import documents.users.AdminMgd;
import documents.users.ClientMgd;
import documents.users.ManagerMgd;
import documents.users.UserMgd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserEnt toDomainModel(UserMgd user) {
        UserEnt userEnt = switch (user) {
            case ManagerMgd _ -> new ManagerEnt(user.getLogin(), user.getPassword(), user.isActive());
            case AdminMgd _ -> new AdminEnt(user.getLogin(), user.getPassword(), user.isActive());
            case ClientMgd _ -> new ClientEnt(user.getLogin(), user.getPassword(), user.isActive());

            default -> throw new IllegalStateException("Unexpected value: " + user);
        };
        userEnt.setId(user.getId());
        return userEnt;
    }

    public UserMgd toMongoModel(UserEnt user) {
        return switch (user) {
            case ManagerEnt _ -> new ManagerMgd(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
            case AdminEnt _ -> new AdminMgd(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
            case ClientEnt _ -> new ClientMgd(user.getId(), user.getLogin(), user.getPassword(), user.isActive());

            default -> throw new IllegalStateException("Unexpected value: " + user);
        };
    }


    public List<UserEnt> toDomainModel(List<UserMgd> users){
        return users.stream().map(this::toDomainModel).toList();
    }
    public List<UserMgd> toMongoModel(List<UserEnt> users){
        return users.stream().map(this::toMongoModel).toList();
    }
}
