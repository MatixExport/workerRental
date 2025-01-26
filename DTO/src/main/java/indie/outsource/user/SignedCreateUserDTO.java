package indie.outsource.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.UUID;

@Getter
@Setter
public class SignedCreateUserDTO extends CreateUserDTO {

    @NotEmpty
    @NotBlank
    private String signature;

    public SignedCreateUserDTO(String login, String password, USERTYPE type) {
        super(login, password, type);
    }

    @JsonIgnore
    public String getPayload() {
        return getLogin() + getType().name();
    }
}
