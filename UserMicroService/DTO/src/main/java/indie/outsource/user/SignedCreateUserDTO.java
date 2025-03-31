package indie.outsource.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignedCreateUserDTO extends CreateUserDTO {
    
    private String signature;

    public SignedCreateUserDTO(String login, String password, USERTYPE type) {
        super(login, password, type);
    }

    @JsonIgnore
    public String getPayload() {
        return getLogin() + getType().name();
    }
}
