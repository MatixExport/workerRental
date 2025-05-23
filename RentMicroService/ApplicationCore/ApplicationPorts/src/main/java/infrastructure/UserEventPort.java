package infrastructure;

import entities.user.UserEnt;

import java.util.UUID;

public interface UserEventPort {
    void publishRemoveUserEvent(UserEnt userEnt);
}
