package aggregates.mappers;


import entities.user.UserEnt;
import documents.users.UserMgd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserEnt toDomainModel(UserMgd user) {
        return new UserEnt(user.getId(), user.getLogin());
    }

    public UserMgd toMongoModel(UserEnt user) {
        return new UserMgd(user.getId(), user.getLogin());
    }


    public List<UserEnt> toDomainModel(List<UserMgd> users){
        return users.stream().map(this::toDomainModel).toList();
    }
    public List<UserMgd> toMongoModel(List<UserEnt> users){
        return users.stream().map(this::toMongoModel).toList();
    }
}
