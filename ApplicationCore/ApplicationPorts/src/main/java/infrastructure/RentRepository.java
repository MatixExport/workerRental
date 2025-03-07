package infrastructure;


import Entities.RentEnt;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface RentRepository  {

    List<RentEnt> findAll();
    Optional<RentEnt> findById(UUID id);
    RentEnt save(RentEnt rent) throws ResourceNotFoundException, RentAlreadyEndedException, WorkerRentedException;
    void delete(RentEnt rent) throws WorkerRentedException;
    void deleteById(UUID id);
    void deleteAll();
    List<RentEnt> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date);
    List<RentEnt> findByUser_IdAndEndDateIsNull(UUID id);
    List<RentEnt> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date);
    List<RentEnt> findByWorker_IdAndEndDateIsNull(UUID id);
    boolean existsByWorker_IdAndEndDateIsNull(UUID id);
}
