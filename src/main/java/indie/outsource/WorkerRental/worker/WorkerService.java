package indie.outsource.WorkerRental.worker;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.rent.RentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class WorkerService {

    private WorkerRepository workerRepository;
    private RentRepository rentRepository;

    public Worker findById(Long id) {
        if(workerRepository.findById(id).isPresent()){
            return workerRepository.findById(id).get();
        }
        else
            throw new ResourceNotFoundException();
    }

    public List<Worker> findAll() {
        return (List<Worker>) workerRepository.findAll();
    }

    public Worker save(Worker worker) {
        return workerRepository.save(worker);
    }

    public void delete(Long id) {
        if(workerRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException();
        }
        if(rentRepository.existsByWorker_IdAndEndDateIsNotNull(id)){
            throw new WorkerRentedException();
        }
       workerRepository.deleteById(id);
    }
}
