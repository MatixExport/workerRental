package spring.dto.mappers;

import entities.WorkerEnt;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;

public final class WorkerMapper {
    public static WorkerDTO getWorkerDto(WorkerEnt worker) {
        return new WorkerDTO(worker.getName(), worker.getId());
    }

    public static WorkerEnt getWorker(CreateWorkerDTO createWorkerDTO) {
        WorkerEnt worker = new WorkerEnt();
        worker.setName(createWorkerDTO.getName());
        return worker;
    }
}
