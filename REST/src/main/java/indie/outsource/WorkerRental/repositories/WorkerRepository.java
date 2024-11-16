package indie.outsource.WorkerRental.repositories;


import indie.outsource.WorkerRental.model.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Component
@Repository
public interface WorkerRepository extends BaseRepository<Worker, UUID> {

}
