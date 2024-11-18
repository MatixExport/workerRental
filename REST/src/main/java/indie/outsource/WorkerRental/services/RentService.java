package indie.outsource.WorkerRental.services;

import indie.outsource.WorkerRental.exceptions.*;
import indie.outsource.WorkerRental.model.Rent;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.repositories.RentRepository;
import indie.outsource.WorkerRental.repositories.UserRepository;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RentService {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;


    public Rent findById(UUID id) {
        if(rentRepository.findById(id).isPresent()){
            return rentRepository.findById(id).get();
        }
        else
            throw new ResourceNotFoundException("Rent not found");
    }

    public List<Rent> findAll() {
        return (List<Rent>) rentRepository.findAll();
    }

    public Rent createRent(UUID clientId, UUID workerId, LocalDateTime startDate) {
        Optional<User> user = userRepository.findById(clientId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        if(! user.get().isActive()){
            throw new UserInactiveException();
        }
        Optional<Worker> worker = workerRepository.findById(workerId);
        if(worker.isEmpty()){
            throw new ResourceNotFoundException("Worker not found");
        }
        if(rentRepository.existsByWorker_IdAndEndDateIsNull(workerId)){
            throw new WorkerRentedException();
        }
        Rent rent = new Rent();
        rent.setUser(user.get());
        rent.setWorker(worker.get());
        rent.setStartDate(startDate);
        return rentRepository.save(rent);
    }

    public List<Rent> getClientActiveRents(UUID clientId){
        if(rentRepository.findById(clientId).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        return rentRepository.findByUser_IdAndEndDateIsNull(clientId);
    }
    public List<Rent> getClientEndedRents(UUID clientId){
        if(rentRepository.findById(clientId).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        return rentRepository.findByUser_IdAndEndDateBefore(clientId, LocalDateTime.now());
    }

    public List<Rent> getWorkerActiveRents(UUID workerId){
        if(rentRepository.findById(workerId).isEmpty()){
            throw new ResourceNotFoundException("Worker not found");
        }
        return rentRepository.findByWorker_IdAndEndDateIsNull(workerId);
    }
    public List<Rent> getWorkerEndedRents(UUID workerId){
        if(rentRepository.findById(workerId).isEmpty()){
            throw new ResourceNotFoundException("Worker not found");
        }
        return rentRepository.findByWorker_IdAndEndDateBefore(workerId, LocalDateTime.now());
    }

    public Rent endRent(UUID rentId){
        if(rentRepository.findById(rentId).isEmpty()){
            throw new ResourceNotFoundException();
        }
        Rent rent = rentRepository.findById(rentId).get();
        if(rent.getEndDate() != null){
            throw new RentAlreadyEndedException();
        }
        rent.setEndDate(LocalDateTime.now());
        return rentRepository.save(rent);
    }
    public void deleteRent(UUID rentId){
        if(rentRepository.findById(rentId).isEmpty()){
            throw new ResourceNotFoundException();
        }
        if(rentRepository.findById(rentId).get().getEndDate() != null){
            throw new RentAlreadyEndedException();
        }
        rentRepository.deleteById(rentId);
    }
}
