package indie.outsource.WorkerRental.documents.user;


import indie.outsource.WorkerRental.model.user.Client;
import indie.outsource.WorkerRental.model.user.Manager;
import indie.outsource.WorkerRental.model.user.User;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
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

    public Manager toDomainModel(){
        Manager manager = new Manager();
        manager.setId(getId());
        manager.setLogin(getLogin());
        manager.setPassword(getPassword());
        manager.setActive(isActive());
        return manager;
    }
}
