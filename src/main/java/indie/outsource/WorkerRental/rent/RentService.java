package indie.outsource.WorkerRental.rent;

import indie.outsource.WorkerRental.user.User;
import indie.outsource.WorkerRental.user.UserRepository;
import indie.outsource.WorkerRental.worker.Worker;
import indie.outsource.WorkerRental.worker.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class RentService {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;


    public Rent findById(Long id) {
        if(rentRepository.findById(id).isPresent()){
            return rentRepository.findById(id).get();
        }
        else
            throw new RuntimeException("Resource not found");
    }

    public Rent createRent(Long clientId, Long workerId, LocalDateTime startDate) {
        Optional<User> user = userRepository.findById(clientId);
        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }
        if(! user.get().isActive()){
            throw new RuntimeException("User is not active");
        }
        Optional<Worker> worker = workerRepository.findById(workerId);
        if(worker.isEmpty()){
            throw new RuntimeException("Worker not found");
        }
        if(rentRepository.existsByWorker_IdAndEndDateIsNotNull(workerId)){
            throw new RuntimeException("Worker occupied");
        }
        Rent rent = new Rent();
        rent.setUser(user.get());
        rent.setWorker(worker.get());
        rent.setStartDate(startDate);
        return rentRepository.save(rent);
    }

    public List<Rent> getClientRents(Long clientId){
        return rentRepository.findByUser_IdAndEndDateIsNull(clientId);
    }
    public List<Rent> getClientEndedRents(Long clientId){
        return rentRepository.findByUser_IdAndEndDateBefore(clientId, LocalDateTime.now());
    }

    public List<Rent> getWorkerRents(Long workerId){
        return rentRepository.findByWorker_IdAndEndDateIsNull(workerId);
    }
    public List<Rent> getWorkerEndedRents(Long workerId){
        return rentRepository.findByWorker_IdAndEndDateBefore(workerId, LocalDateTime.now());
    }

    public Rent endRent(Long rentId){
        if(rentRepository.findById(rentId).isEmpty()){
            throw new RuntimeException("Resource not found");
        }
        Rent rent = rentRepository.findById(rentId).get();
        if(rent.getEndDate() != null){
            throw new RuntimeException("End date is already set");
        }
        rent.setEndDate(LocalDateTime.now());
        return rentRepository.save(rent);
    }

    public void deleteRent(Long rentId){
        if(rentRepository.findById(rentId).isEmpty()){
            throw new RuntimeException("Resource not found");
        }
        if(rentRepository.findById(rentId).get().getEndDate() != null){
            throw new RuntimeException("Rent is finished, cannot delete it");
        }
        rentRepository.deleteById(rentId);
    }
}
