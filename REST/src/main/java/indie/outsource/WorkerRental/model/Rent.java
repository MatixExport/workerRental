package indie.outsource.WorkerRental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Rent extends AbstractEntity {

    //https://www.baeldung.com/javax-validation-method-constraints
    @NotNull
    LocalDateTime startDate;

    LocalDateTime endDate;

    @OneToOne
    Worker worker;

    @OneToOne
    User user;
}
