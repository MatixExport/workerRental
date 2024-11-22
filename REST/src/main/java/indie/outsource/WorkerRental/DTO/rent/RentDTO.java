package indie.outsource.WorkerRental.DTO.rent;

import lombok.*;

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
