package documents;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractEntityMgd {
    @BsonId
    @BsonProperty(FieldsConsts.ENTITY_ID)
    private UUID id;

    @BsonCreator
    protected AbstractEntityMgd(
         @BsonProperty(FieldsConsts.ENTITY_ID) UUID id
    ) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntityMgd that = (AbstractEntityMgd) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
