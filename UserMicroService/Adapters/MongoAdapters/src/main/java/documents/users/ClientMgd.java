package documents.users;


import documents.FieldsConsts;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class ClientMgd extends UserMgd {
    @BsonCreator
    public ClientMgd(
            @BsonProperty(FieldsConsts.ENTITY_ID) UUID id,
            @BsonProperty(FieldsConsts.ACCOUNT_LOGIN) String login,
            @BsonProperty(FieldsConsts.ACCOUNT_PASSWORD) String password,
            @BsonProperty(FieldsConsts.ACCOUNT_ACTIVE) boolean active) {
        super(id, login, password, active);
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
