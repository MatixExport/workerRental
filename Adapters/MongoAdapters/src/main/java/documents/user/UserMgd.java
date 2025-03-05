package documents.user;

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
public abstract class UserMgd extends AbstractEntityMgd {

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


    public void removePassword() {
        this.password = null;
    }


}
