package indie.outsource.ApplicationCore.documents.user;


import indie.outsource.ApplicationCore.model.user.Admin;
import indie.outsource.ApplicationCore.model.user.User;
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

    public AdminMgd(User user) {
        super(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
    }

    @Override
    public Admin toDomainModel(){
        Admin admin = new Admin();
        admin.setId(getId());
        admin.setLogin(getLogin());
        admin.setPassword(getPassword());
        admin.setActive(isActive());
        return admin;
    }
}
