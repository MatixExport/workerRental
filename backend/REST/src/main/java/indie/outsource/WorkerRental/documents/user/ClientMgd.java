package indie.outsource.WorkerRental.documents.user;

import indie.outsource.WorkerRental.model.user.Admin;
import indie.outsource.WorkerRental.model.user.Client;
import indie.outsource.WorkerRental.model.user.User;
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
    public ClientMgd(User user) {
        super(user.getId(), user.getLogin(), user.getPassword(), user.isActive());
    }

    @Override
    public Client toDomainModel(){
        Client client = new Client();
        client.setId(getId());
        client.setLogin(getLogin());
        client.setPassword(getPassword());
        client.setActive(isActive());
        return client;
    }
}
