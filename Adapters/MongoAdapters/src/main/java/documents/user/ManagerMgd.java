package documents.user;

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

}
