package indie.outsource.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto extends SignedCreateUserDTO{

    private String oldPassword;

    public ChangePasswordDto(String login, String password, USERTYPE type) {
        super(login, password, type);
    }
}
