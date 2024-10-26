package indie.outsource.WorkerRental.worker;

import indie.outsource.WorkerRental.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Worker extends AbstractEntity {

    @NotNull
    @Column(unique = true)
    @NotBlank @NotEmpty
    private String name;
}
