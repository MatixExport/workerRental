package indie.outsource.WorkerRental.documents.user;

import indie.outsource.WorkerRental.documents.AbstractEntityMgd;
import indie.outsource.WorkerRental.model.user.Admin;
import indie.outsource.WorkerRental.model.user.Client;
import indie.outsource.WorkerRental.model.user.Manager;
import indie.outsource.WorkerRental.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@BsonDiscriminator()
public class UserMgd extends AbstractEntityMgd {

    @BsonProperty("login")
    private String login;
    @BsonProperty("password")
    private String password;
    @BsonProperty("active")
    private boolean active;

    @BsonCreator
    public UserMgd(
            @BsonProperty("_id") UUID id,
            @BsonProperty("login") String login,
            @BsonProperty("password") String password,
            @BsonProperty("active") boolean active) {
        super(id);
        this.login = login;
        this.password = password;
        this.active = active;
    }

    public UserMgd(User user) {
        super(user.getId());
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.active = user.isActive();
    }

    public void removePassword() {
        this.password = null;
    }

    public User toDomainModel(){
        User user = new User();
        user.setId(getId());
        user.setLogin(getLogin());
        user.setPassword(getPassword());
        user.setActive(isActive());
        return user;
    }
    public static UserMgd fromDomainModel(User user){
        if(user.getClass().equals(Manager.class)){
            return new ManagerMgd(user);
        }
        if(user.getClass().equals(Admin.class)){
            return new AdminMgd(user);
        }
        if(user.getClass().equals(Client.class)){
            return new ClientMgd(user);
        }
        return new UserMgd(user);
    }
}
