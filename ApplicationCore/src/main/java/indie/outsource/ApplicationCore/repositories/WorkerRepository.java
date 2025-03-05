package indie.outsource.ApplicationCore.repositories;


import indie.outsource.ApplicationCore.model.Worker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Component
@Repository
public interface WorkerRepository extends BaseRepository<Worker, UUID> {

}
