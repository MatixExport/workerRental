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

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
