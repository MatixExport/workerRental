package documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;
import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
public class WorkerMgd extends AbstractEntityMgd {
    @BsonProperty(FieldsConsts.WORKER_NAME)
    private String name;
    @BsonProperty(FieldsConsts.WORKER_IS_RENTED)
    private int isRented;

    @BsonCreator
    public WorkerMgd(
           @BsonProperty(FieldsConsts.ENTITY_ID) UUID id,
           @BsonProperty(FieldsConsts.WORKER_NAME) String name,
           @BsonProperty(FieldsConsts.WORKER_IS_RENTED) int isRented
    ) {
        super(id);
        this.name = name;
        this.isRented = isRented;
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
