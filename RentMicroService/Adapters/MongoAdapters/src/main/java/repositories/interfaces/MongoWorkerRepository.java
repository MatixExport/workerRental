package repositories.interfaces;



import documents.WorkerMgd;
import exceptions.WorkerRentedException;

import java.util.List;
import java.util.UUID;


public interface MongoWorkerRepository {
    List<WorkerMgd> findAll();
    WorkerMgd findById(UUID id);
    WorkerMgd save(WorkerMgd worker) ;
    void delete(WorkerMgd worker) throws WorkerRentedException;
    void deleteById(UUID id) throws WorkerRentedException;
    void deleteAll();
}
