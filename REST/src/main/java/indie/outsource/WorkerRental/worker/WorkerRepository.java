package indie.outsource.WorkerRental.worker;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, UUID> {

}
