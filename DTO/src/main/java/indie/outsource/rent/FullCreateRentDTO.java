package indie.outsource.rent;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class FullCreateRentDTO {
    @FutureOrPresent
    LocalDateTime startDate;

    @NotNull
    UUID userID;

    @NotNull
    UUID workerID;
}
