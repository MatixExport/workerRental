package indie.outsource.WorkerRental.model;

import indie.outsource.WorkerRental.model.user.User;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class Rent extends AbstractEntity {

    //https://www.baeldung.com/javax-validation-method-constraints
    @NotNull
    LocalDateTime startDate;

    LocalDateTime endDate;

    Worker worker;

    User user;
}
