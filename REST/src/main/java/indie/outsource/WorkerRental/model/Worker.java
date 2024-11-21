package indie.outsource.WorkerRental.model;


import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Worker extends AbstractEntity {

    @NotBlank
    private String name;
}
