package entities.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AdminEnt extends entities.user.UserEnt {
    public AdminEnt(String login, String password, boolean active) {
        super(login, password, active);
    }

    @Override
    public String getGroups(){
        return "ADMIN";
    }
}
