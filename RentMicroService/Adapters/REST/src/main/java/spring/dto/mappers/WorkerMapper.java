package spring.dto.mappers;

import entities.WorkerEnt;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;

public final class WorkerMapper {

    private WorkerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static WorkerDTO getWorkerDto(WorkerEnt worker) {
        return new WorkerDTO(worker.getName(), worker.getId());
    }

    public static WorkerEnt getWorker(CreateWorkerDTO createWorkerDTO) {
        WorkerEnt worker = new WorkerEnt();
        worker.setName(createWorkerDTO.getName());
        return worker;
    }
}
