package indie.outsource.WorkerRental.DTO.user;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String login;
    private boolean active;
    private UUID id;

    USERTYPE type;
}
