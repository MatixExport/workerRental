package indie.outsource.WorkerRental.repositories;


import indie.outsource.WorkerRental.model.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, UUID> {

}
