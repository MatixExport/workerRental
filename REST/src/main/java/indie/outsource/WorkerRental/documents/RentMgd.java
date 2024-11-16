package indie.outsource.WorkerRental.documents;

import indie.outsource.WorkerRental.documents.user.UserMgd;
import indie.outsource.WorkerRental.model.Rent;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class RentMgd extends AbstractEntityMgd {

    //https://www.baeldung.com/javax-validation-method-constraints

    @BsonProperty
    LocalDateTime startDate;
    @BsonProperty
    LocalDateTime endDate;
    @BsonProperty
    WorkerMgd worker;
    @BsonProperty(useDiscriminator = true)
    UserMgd user;

    public RentMgd(
            @BsonProperty("_id") UUID id,
            @BsonProperty LocalDateTime startDate,
            @BsonProperty LocalDateTime endDate,
            @BsonProperty WorkerMgd worker,
            @BsonProperty UserMgd user) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.worker = worker;
        this.user = user;
    }

    public RentMgd(Rent rent){
        super(rent.getId());
        this.startDate = rent.getStartDate();
        this.endDate = rent.getEndDate();
        this.worker = new WorkerMgd(rent.getWorker());
        this.user = UserMgd.fromDomainModel(rent.getUser());
    }

    public Rent toDomainModel(){
        Rent rent = new Rent();
        rent.setId(getId());
        rent.setStartDate(getStartDate());
        rent.setEndDate(getEndDate());
        rent.setWorker(this.worker.toDomainModel());
        rent.setUser(this.user.toDomainModel());
        return rent;
    }
}
