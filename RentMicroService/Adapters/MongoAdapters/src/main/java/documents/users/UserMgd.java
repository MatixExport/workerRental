package documents.users;

import documents.AbstractEntityMgd;
import documents.FieldsConsts;
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

    @BsonProperty(FieldsConsts.ACCOUNT_LOGIN)
    private String login;

    @BsonCreator
    public UserMgd(
            @BsonProperty(FieldsConsts.ENTITY_ID) UUID id,
            @BsonProperty(FieldsConsts.ACCOUNT_LOGIN) String login) {
        super(id);
        this.login = login;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
