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
public abstract class UserMgd extends AbstractEntityMgd {

    @BsonProperty(FieldsConsts.ACCOUNT_LOGIN)
    private String login;
    @BsonProperty(FieldsConsts.ACCOUNT_PASSWORD)
    private String password;
    @BsonProperty(FieldsConsts.ACCOUNT_ACTIVE)
    private boolean active;

    @BsonCreator
    protected UserMgd(
            @BsonProperty(FieldsConsts.ENTITY_ID) UUID id,
            @BsonProperty(FieldsConsts.ACCOUNT_LOGIN) String login,
            @BsonProperty(FieldsConsts.ACCOUNT_PASSWORD) String password,
            @BsonProperty(FieldsConsts.ACCOUNT_ACTIVE) boolean active) {
        super(id);
        this.login = login;
        this.password = password;
        this.active = active;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }




    public void removePassword() {
        this.password = null;
    }


}
