package documents.user;

import Entities.user.ClientEnt;

import Entities.user.UserEnt;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class ClientMgd extends UserMgd {
    @BsonCreator
    public ClientMgd(
           @BsonProperty("_id") UUID id,
           @BsonProperty("login") String login,
           @BsonProperty("password")  String password,
           @BsonProperty("active") boolean active) {
        super(id, login, password, active);
    }
    public ClientMgd(UserEnt user) {
        super(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
    }

    @Override
    public ClientEnt toDomainModel(){
        ClientEnt client = new ClientEnt();
        client.setId(getId());
        client.setLogin(getLogin());
        client.setPassword(getPassword());
        client.setActive(isActive());
        return client;
    }
}
