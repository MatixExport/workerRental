package indie.outsource.WorkerRental.model;


import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Worker extends AbstractEntity {

    @NotBlank
    private String name;
}
