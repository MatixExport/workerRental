package indie.outsource.WorkerRental.model.user;

import indie.outsource.user.USERTYPE;
import lombok.AllArgsConstructor;
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
