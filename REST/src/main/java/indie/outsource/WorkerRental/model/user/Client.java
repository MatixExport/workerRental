package indie.outsource.WorkerRental.model.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Client extends User{
    public Client(String login, String password, boolean active) {
        super(login, password, active);
    }
}
