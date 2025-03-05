package repositories.interfaces;



import documents.WorkerMgd;
import exceptions.WorkerRentedException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Component
@Repository
public interface MongoWorkerRepository {
    List<WorkerMgd> findAll();
    WorkerMgd findById(UUID id);
    WorkerMgd save(WorkerMgd worker) ;
    void delete(WorkerMgd worker) throws WorkerRentedException;
    void deleteById(UUID id) throws WorkerRentedException;
    void deleteAll();
}
