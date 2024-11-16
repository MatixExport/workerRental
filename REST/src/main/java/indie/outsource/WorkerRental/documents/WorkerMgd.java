package indie.outsource.WorkerRental.documents;

import indie.outsource.WorkerRental.model.Worker;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
public class WorkerMgd extends AbstractEntityMgd {
    @BsonProperty
    private String name;

    @BsonCreator
    public WorkerMgd(
           @BsonProperty("_id") UUID id,
           @BsonProperty String name) {
        super(id);
        this.name = name;
    }
    public WorkerMgd(Worker worker) {
        super(worker.getId());
        this.name = worker.getName();
    }
    public Worker toDomainModel(){
        Worker worker = new Worker();
        worker.setId(getId());
        worker.setName(getName());
        return worker;
    }
}
