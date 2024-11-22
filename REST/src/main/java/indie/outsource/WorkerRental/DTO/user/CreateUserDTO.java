package indie.outsource.WorkerRental.DTO.user;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String login;

    @NotBlank
    @Size(min = 2, max = 50)
    private String password;

    @NotNull
    USERTYPE type;
}
