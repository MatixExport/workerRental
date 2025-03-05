package indie.outsource.ApplicationCore.model.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Manager extends User {
    public Manager(String login, String password, boolean active) {
        super(login, password, active);
    }

    @Override
    public String getGroups(){
        return "MANAGER";
    }
}
