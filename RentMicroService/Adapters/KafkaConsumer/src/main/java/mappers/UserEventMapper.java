package mappers;

import entities.user.UserEnt;
import indie.outsource.event.user.UserEvent;

public class UserEventMapper {
    public static UserEnt toDomainModel(UserEvent userEvent){
        UserEnt userEnt = new UserEnt();
        userEnt.setId(userEvent.id());
        userEnt.setLogin(userEvent.login());
        return userEnt;
    }
}
