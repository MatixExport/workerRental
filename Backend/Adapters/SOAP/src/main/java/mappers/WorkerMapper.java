package mappers;

import entities.WorkerEnt;
import com.example.soap.Worker;

import java.util.UUID;

public class WorkerMapper {
    public static Worker fromDomainModel(WorkerEnt workerEnt) {
        Worker worker = new Worker();
        worker.setName(workerEnt.getName());
        worker.setId(workerEnt.getId().toString());
        return worker;
    }
    public static WorkerEnt toDomainModel(Worker worker) {
        WorkerEnt workerEnt = new WorkerEnt();
        workerEnt.setName(worker.getName());
        if(worker.getId() != null) {
            workerEnt.setId(UUID.fromString(worker.getId()));
        }
        return workerEnt;
    }
}
