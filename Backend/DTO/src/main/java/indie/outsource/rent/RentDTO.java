package indie.outsource.rent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentDTO {
    LocalDateTime startDate;
    LocalDateTime endDate;
    private UUID userID;
    private UUID workerID;
    private UUID id;
}
