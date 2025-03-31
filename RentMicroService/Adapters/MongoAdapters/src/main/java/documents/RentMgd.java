package documents;


import documents.users.UserMgd;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class RentMgd extends AbstractEntityMgd {

    @BsonProperty(FieldsConsts.RENT_START_DATE)
    LocalDateTime startDate;
    @BsonProperty(FieldsConsts.RENT_END_DATE)
    LocalDateTime endDate;
    @BsonProperty(FieldsConsts.RENT_WORKER)
    WorkerMgd worker;
    @BsonProperty(value = FieldsConsts.RENT_USER,useDiscriminator = true)
    UserMgd user;

    @BsonCreator
    public RentMgd(
            @BsonProperty(FieldsConsts.ENTITY_ID) UUID id,
            @BsonProperty(FieldsConsts.RENT_START_DATE) LocalDateTime startDate,
            @BsonProperty(FieldsConsts.RENT_END_DATE) LocalDateTime endDate,
            @BsonProperty(FieldsConsts.RENT_WORKER) WorkerMgd worker,
            @BsonProperty(FieldsConsts.RENT_USER) UserMgd user) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.worker = worker;
        this.user = user;
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
