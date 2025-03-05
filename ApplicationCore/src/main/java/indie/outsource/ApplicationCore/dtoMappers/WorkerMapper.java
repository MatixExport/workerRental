package indie.outsource.ApplicationCore.dtoMappers;

import indie.outsource.ApplicationCore.model.Worker;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;

public final class WorkerMapper {
    public static WorkerDTO getWorkerDto(Worker worker) {
        return new WorkerDTO(worker.getName(), worker.getId());
    }

    public static Worker getWorker(CreateWorkerDTO createWorkerDTO) {
        Worker worker = new Worker();
        worker.setName(createWorkerDTO.getName());
        return worker;
    }
}
