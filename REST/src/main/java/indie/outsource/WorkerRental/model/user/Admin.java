package indie.outsource.WorkerRental.model.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Admin extends User {
    public Admin(String login, String password, boolean active) {
        super(login, password, active);
    }
}
