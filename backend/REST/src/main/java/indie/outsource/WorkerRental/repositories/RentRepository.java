package indie.outsource.WorkerRental.repositories;

import indie.outsource.WorkerRental.model.Rent;
import jakarta.annotation.Priority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Repository
@Priority(10)

public interface RentRepository extends BaseRepository<Rent, UUID> {

    List<Rent> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date);
    List<Rent> findByUser_IdAndEndDateIsNull(UUID id);
    List<Rent> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date);
    List<Rent> findByWorker_IdAndEndDateIsNull(UUID id);
    boolean existsByWorker_IdAndEndDateIsNull(UUID id);
}
