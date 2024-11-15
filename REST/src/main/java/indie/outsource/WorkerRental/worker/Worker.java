package indie.outsource.WorkerRental.worker;

import indie.outsource.WorkerRental.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Worker extends AbstractEntity {
    @Column(unique = true)
    @NotBlank
    private String name;
}
