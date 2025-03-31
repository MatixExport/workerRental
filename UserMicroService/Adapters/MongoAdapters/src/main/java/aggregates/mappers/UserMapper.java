package aggregates.mappers;

import entities.user.AdminEnt;
import entities.user.UserEnt;
import documents.users.AdminMgd;
import documents.users.UserMgd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserEnt toDomainModel(UserMgd user) {
        UserEnt userEnt = switch (user) {
            case AdminMgd _ -> new AdminEnt(user.getLogin(), user.getPassword(), user.isActive());
            default -> new UserEnt(user.getLogin(), user.getPassword(), user.isActive()) ;
        };
        userEnt.setId(user.getId());
        return userEnt;
    }

    public UserMgd toMongoModel(UserEnt user) {
        return switch (user) {
            case AdminEnt _ -> new AdminMgd(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
            default -> new UserMgd(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
        };
    }


    public List<UserEnt> toDomainModel(List<UserMgd> users){
        return users.stream().map(this::toDomainModel).toList();
    }
    public List<UserMgd> toMongoModel(List<UserEnt> users){
        return users.stream().map(this::toMongoModel).toList();
    }
}
