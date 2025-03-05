package indie.outsource.ApplicationCore.model.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Client extends User{
    public Client(String login, String password, boolean active) {
        super(login, password, active);
    }

    @Override
    public String getGroups(){
        return "CLIENT";
    }
}
