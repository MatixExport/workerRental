package indie.outsource.WorkerRental.dtoMappers;

import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.DTO.worker.CreateWorkerDTO;
import indie.outsource.WorkerRental.DTO.worker.WorkerDTO;

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
