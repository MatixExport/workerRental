package view;

import entities.WorkerEnt;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface WorkerService {

    WorkerEnt findById(UUID id) throws ResourceNotFoundException;

    List<WorkerEnt> findAll();

    WorkerEnt save(WorkerEnt worker);

    WorkerEnt updateWorker(WorkerEnt worker) throws ResourceNotFoundException;

    void delete(UUID id) throws ResourceNotFoundException, WorkerRentedException;
}
