package indie.outsource.ApplicationCore.model.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Admin extends User {
    public Admin(String login, String password, boolean active) {
        super(login, password, active);
    }

    @Override
    public String getGroups(){
        return "ADMIN";
    }
}
