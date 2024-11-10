package indie.outsource.rent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class RentDTO {

    //https://www.baeldung.com/javax-validation-method-constraints
    LocalDateTime startDate;
    LocalDateTime endDate;
    private Long userID;
    private Long workerID;
}
