package indie.outsource.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
