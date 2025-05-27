package mappers;

import entities.user.UserEnt;
import indie.outsource.event.user.UserEvent;

public class UserEventMapper {
    public static UserEvent fromDomainModel(UserEnt userEnt) {
        return new UserEvent(userEnt.getLogin(),userEnt.getId());
    }
}
