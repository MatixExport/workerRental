package infrastructure;

import entities.user.UserEnt;

public interface UserEventPort {
    void publishCreateUserEvent(UserEnt userEnt);
}
