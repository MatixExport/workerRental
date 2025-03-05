package indie.outsource.ApplicationCore.documents.user;


import indie.outsource.ApplicationCore.model.user.Manager;
import indie.outsource.ApplicationCore.model.user.User;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class ManagerMgd extends UserMgd {
    @BsonCreator
    public ManagerMgd(
            @BsonProperty("_id") UUID id,
            @BsonProperty("login") String login,
            @BsonProperty("password") String password,
            @BsonProperty("active") boolean active
    ) {
        super(id, login, password, active);
    }

    public ManagerMgd(User user) {
        super(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
    }

    @Override
    public Manager toDomainModel(){
        Manager manager = new Manager();
        manager.setId(getId());
        manager.setLogin(getLogin());
        manager.setPassword(getPassword());
        manager.setActive(isActive());
        return manager;
    }
}
