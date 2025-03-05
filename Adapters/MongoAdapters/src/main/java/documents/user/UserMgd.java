package documents.user;

import Entities.user.AdminEnt;
import Entities.user.ClientEnt;
import Entities.user.ManagerEnt;
import Entities.user.UserEnt;
import documents.AbstractEntityMgd;

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

    public UserMgd(UserEnt user) {
        super(user.getId());
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.active = user.isActive();
    }

    public void removePassword() {
        this.password = null;
    }

    public UserEnt toDomainModel(){
        return null;
    }
    public static UserMgd fromDomainModel(UserEnt user){
        if(user.getClass().equals(ManagerEnt.class)){
            return new ManagerMgd(user);
        }
        if(user.getClass().equals(AdminEnt.class)){
            return new AdminMgd(user);
        }
        if(user.getClass().equals(ClientEnt.class)){
            return new ClientMgd(user);
        }
        return new UserMgd(user);
    }
}
