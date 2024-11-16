package indie.outsource.WorkerRental.documents;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractEntityMgd {
    @BsonId
    @BsonProperty("_id")
    private UUID id;

    @BsonCreator
    public AbstractEntityMgd(
         @BsonProperty("_id") UUID id
    ) {
        this.id = id;
    }
}
