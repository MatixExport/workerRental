package indie.outsource.WorkerRental.rent;

import jakarta.annotation.Priority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Priority(10)
public interface RentRepository extends CrudRepository<Rent, Long> {

    List<Rent> findByUser_IdAndEndDateBefore(Long id, LocalDateTime date);
    List<Rent> findByUser_IdAndEndDateIsNull(Long id);

    List<Rent> findByWorker_IdAndEndDateBefore(Long id, LocalDateTime date);
    List<Rent> findByWorker_IdAndEndDateIsNull(Long id);

    boolean existsByWorker_IdAndEndDateIsNotNull(Long id);
}
