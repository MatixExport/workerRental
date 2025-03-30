package entities.user;

import entities.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserEnt extends AbstractEntity {

    private String login;
    private String password;
    private boolean active;

    public String getGroups(){
        return null;
    }

}
