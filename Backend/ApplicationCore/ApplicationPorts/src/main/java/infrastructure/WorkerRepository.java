package infrastructure;


import Entities.WorkerEnt;
import exceptions.WorkerRentedException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface WorkerRepository {
    List<WorkerEnt> findAll();
    Optional<WorkerEnt> findById(UUID id);
    WorkerEnt save(WorkerEnt worker) ;
    void delete(WorkerEnt worker) throws WorkerRentedException;
    void deleteById(UUID id) throws WorkerRentedException;
    void deleteAll();
}
