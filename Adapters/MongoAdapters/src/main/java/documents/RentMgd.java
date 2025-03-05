package documents;


import Entities.RentEnt;
import documents.user.UserMgd;
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
    @BsonProperty(value = "Entities.user",useDiscriminator = true)
    UserMgd user;

    @BsonCreator
    public RentMgd(
            @BsonProperty("_id") UUID id,
            @BsonProperty("startDate") LocalDateTime startDate,
            @BsonProperty("endDate") LocalDateTime endDate,
            @BsonProperty("worker") WorkerMgd worker,
            @BsonProperty("Entities.user") UserMgd user) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.worker = worker;
        this.user = user;
    }

    public RentMgd(RentEnt rent){
        super(rent.getId());
        this.startDate = rent.getStartDate();
        this.endDate = rent.getEndDate();
        this.worker = new WorkerMgd(rent.getWorker());
        this.user = UserMgd.fromDomainModel(rent.getUser());
    }

    public RentEnt toDomainModel(){
        RentEnt rent = new RentEnt();
        rent.setId(getId());
        rent.setStartDate(getStartDate());
        rent.setEndDate(getEndDate());
        rent.setWorker(this.worker.toDomainModel());
        rent.setUser(this.user.toDomainModel());
        return rent;
    }
}
