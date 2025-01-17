package indie.outsource.WorkerRental.model.user;

import indie.outsource.WorkerRental.model.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class User extends AbstractEntity {

    private String login;
    private String password;
    private boolean active;

    public String getGroups(){
        return null;
    }

}
