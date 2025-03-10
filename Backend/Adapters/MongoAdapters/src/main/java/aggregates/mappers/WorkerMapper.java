package aggregates.mappers;

import Entities.WorkerEnt;
import documents.WorkerMgd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkerMapper {

    public WorkerMgd toMongoModel(WorkerEnt worker) {
        WorkerMgd workerMgd = new WorkerMgd();
        workerMgd.setId(worker.getId());
        workerMgd.setName(worker.getName());
        return workerMgd;
    }
    public WorkerEnt toDomainModel(WorkerMgd workerMgd){
        WorkerEnt worker = new WorkerEnt();
        worker.setId(workerMgd.getId());
        worker.setName(workerMgd.getName());
        return worker;
    }

    public List<WorkerEnt> toDomainModel(List<WorkerMgd> workers){
        return workers.stream().map(this::toDomainModel).toList();
    }
    public List<WorkerMgd> toMongoModel(List<WorkerEnt> workers){
        return workers.stream().map(this::toMongoModel).toList();
    }
}
