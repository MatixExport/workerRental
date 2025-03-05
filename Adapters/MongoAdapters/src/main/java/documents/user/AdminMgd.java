package documents.user;


import Entities.user.AdminEnt;
import Entities.user.UserEnt;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class AdminMgd extends UserMgd {
    @BsonCreator
    public AdminMgd(
            @BsonProperty("_id") UUID id,
            @BsonProperty("login") String login,
            @BsonProperty("password") String password,
            @BsonProperty("active") boolean active) {
        super(id, login, password, active);
    }

    public AdminMgd(UserEnt user) {
        super(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
    }

    @Override
    public AdminEnt toDomainModel(){
        AdminEnt admin = new AdminEnt();
        admin.setId(getId());
        admin.setLogin(getLogin());
        admin.setPassword(getPassword());
        admin.setActive(isActive());
        return admin;
    }
}
