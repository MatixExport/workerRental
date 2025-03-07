package repositories.interfaces;


import documents.RentMgd;
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
public interface MongoRentRepository {

    List<RentMgd> findAll();
    RentMgd findById(UUID id);
    RentMgd save(RentMgd rent) throws ResourceNotFoundException, RentAlreadyEndedException, WorkerRentedException;
    void delete(RentMgd rent) throws WorkerRentedException;
    void deleteById(UUID id);
    void deleteAll();
    List<RentMgd> findByUser_IdAndEndDateBefore(UUID id, LocalDateTime date);
    List<RentMgd> findByUser_IdAndEndDateIsNull(UUID id);
    List<RentMgd> findByWorker_IdAndEndDateBefore(UUID id, LocalDateTime date);
    List<RentMgd> findByWorker_IdAndEndDateIsNull(UUID id);
    boolean existsByWorker_IdAndEndDateIsNull(UUID id);
}
