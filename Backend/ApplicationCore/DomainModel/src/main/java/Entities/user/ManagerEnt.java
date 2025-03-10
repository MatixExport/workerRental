package Entities.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ManagerEnt extends UserEnt {
    public ManagerEnt(String login, String password, boolean active) {
        super(login, password, active);
    }

    @Override
    public String getGroups(){
        return "MANAGER";
    }
}
