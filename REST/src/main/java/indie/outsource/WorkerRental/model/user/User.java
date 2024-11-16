package indie.outsource.WorkerRental.model.user;

import indie.outsource.WorkerRental.model.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends AbstractEntity {

    @Column(unique=true)
    private String login;
    private String password;

    private boolean active;
}
