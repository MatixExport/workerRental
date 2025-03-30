package entities.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClientEnt extends entities.user.UserEnt {
    public ClientEnt(String login, String password, boolean active) {
        super(login, password, active);
    }

    @Override
    public String getGroups(){
        return "CLIENT";
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
