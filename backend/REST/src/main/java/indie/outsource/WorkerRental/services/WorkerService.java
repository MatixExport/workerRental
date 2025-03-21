package indie.outsource.WorkerRental.services;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.exceptions.WorkerRentedException;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.repositories.RentRepository;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class WorkerService {

    private WorkerRepository workerRepository;
    private RentRepository rentRepository;

    public Worker findById(UUID id) {
        if(workerRepository.findById(id).isPresent()){
            return workerRepository.findById(id).get();
        }
        else
            throw new ResourceNotFoundException();
    }

    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    public Worker save(Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker updateWorker(Worker worker){
        if (workerRepository.findById(worker.getId()).isEmpty()){
            throw new ResourceNotFoundException("Worker with id " + worker.getId() + " not found");
        }
        return workerRepository.save(worker);
    }

    public void delete(UUID id) {
        if(workerRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException();
        }
        if(rentRepository.existsByWorker_IdAndEndDateIsNull(id)){
            throw new WorkerRentedException();
        }
       workerRepository.deleteById(id);
    }
}
