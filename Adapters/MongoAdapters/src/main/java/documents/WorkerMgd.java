package documents;

import Entities.WorkerEnt;
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
    @BsonProperty("name")
    private String name;
    @BsonProperty("isRented")
    private int isRented;

    @BsonCreator
    public WorkerMgd(
           @BsonProperty("_id") UUID id,
           @BsonProperty("name") String name,
           @BsonProperty("isRented") int isRented
    ) {
        super(id);
        this.name = name;
        this.isRented = isRented;
    }
    public WorkerMgd(WorkerEnt worker) {
        super(worker.getId());
        this.name = worker.getName();
    }
    public WorkerEnt toDomainModel(){
        WorkerEnt worker = new WorkerEnt();
        worker.setId(getId());
        worker.setName(getName());
        return worker;
    }
}
