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

    @BsonProperty("startDate")
    LocalDateTime startDate;
    @BsonProperty("endDate")
    LocalDateTime endDate;
    @BsonProperty("worker")
    WorkerMgd worker;
    @BsonProperty(value = "user",useDiscriminator = true)
    UserMgd user;

    @BsonCreator
    public RentMgd(
            @BsonProperty("_id") UUID id,
            @BsonProperty("startDate") LocalDateTime startDate,
            @BsonProperty("endDate") LocalDateTime endDate,
            @BsonProperty("worker") WorkerMgd worker,
            @BsonProperty("user") UserMgd user) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.worker = worker;
        this.user = user;
    }
}
