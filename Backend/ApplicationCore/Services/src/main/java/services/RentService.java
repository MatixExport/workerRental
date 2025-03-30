package services;


import Entities.RentEnt;
import Entities.WorkerEnt;
import Entities.user.UserEnt;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WorkerRentedException;
import infrastructure.RentRepository;
import infrastructure.UserRepository;
import infrastructure.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RentService implements view.RentService {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;


    public RentEnt findById(UUID id) throws ResourceNotFoundException {
        Optional<RentEnt> rentEnt = rentRepository.findById(id);
        if(rentEnt.isPresent()){
            return rentEnt.get();
        }
        else
            throw new ResourceNotFoundException("Rent not found");
    }

    public List<RentEnt> findAll() {
        return rentRepository.findAll();
    }

    public RentEnt createRent(UUID clientId, UUID workerId, LocalDateTime startDate) throws ResourceNotFoundException, UserInactiveException, WorkerRentedException {
        Optional<UserEnt> user = userRepository.findById(clientId);
        if(user.isEmpty()){
            System.out.println("User not found");
            throw new ResourceNotFoundException("User not found");
        }
        if(! user.get().isActive()){
            throw new UserInactiveException();
        }
        Optional<WorkerEnt> worker = workerRepository.findById(workerId);
        if(worker.isEmpty()){
            System.out.println("Worker not found");
            throw new ResourceNotFoundException("Worker not found");
        }
        if(rentRepository.existsByWorker_IdAndEndDateIsNull(workerId)){
            throw new WorkerRentedException();
        }
        RentEnt rent = new RentEnt();
        rent.setUser(user.get());
        rent.setWorker(worker.get());
        rent.setStartDate(startDate);
        try {
            return rentRepository.save(rent);
        } catch (RentAlreadyEndedException e) {
            throw new RuntimeException(e);  //TODO fix exceptions
        }
    }

    public List<RentEnt> getClientActiveRents(UUID clientId) throws ResourceNotFoundException {
        if(userRepository.findById(clientId).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        return rentRepository.findByUser_IdAndEndDateIsNull(clientId);
    }
    public List<RentEnt> getClientEndedRents(UUID clientId) throws ResourceNotFoundException {
        if(userRepository.findById(clientId).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        return rentRepository.findByUser_IdAndEndDateBefore(clientId, LocalDateTime.now());
    }

    public List<RentEnt> getWorkerActiveRents(UUID workerId) throws ResourceNotFoundException {
        if(workerRepository.findById(workerId).isEmpty()){
            throw new ResourceNotFoundException("Worker not found");
        }
        return rentRepository.findByWorker_IdAndEndDateIsNull(workerId);
    }
    public List<RentEnt> getWorkerEndedRents(UUID workerId) throws ResourceNotFoundException {
        if(workerRepository.findById(workerId).isEmpty()){
            throw new ResourceNotFoundException("Worker not found");
        }
        return rentRepository.findByWorker_IdAndEndDateBefore(workerId, LocalDateTime.now());
    }

    public RentEnt endRent(UUID rentId) throws ResourceNotFoundException, RentAlreadyEndedException {
        Optional<RentEnt> rentEnt = rentRepository.findById(rentId);
        if(rentEnt.isEmpty()){
            throw new ResourceNotFoundException();
        }
        RentEnt rent = rentEnt.get();
        if(rent.getEndDate() != null){
            throw new RentAlreadyEndedException();
        }
        rent.setEndDate(LocalDateTime.now());
        try {
            return rentRepository.save(rent);
        } catch (WorkerRentedException e) {
            throw new RuntimeException(e);  //TODO fix exceptions
        }
    }
    public void deleteRent(UUID rentId) throws ResourceNotFoundException, RentAlreadyEndedException {
        Optional<RentEnt> rentEnt = rentRepository.findById(rentId);
        if(rentEnt.isEmpty()){
            throw new ResourceNotFoundException();
        }
        if(rentEnt.get().getEndDate() != null){
            throw new RentAlreadyEndedException();
        }
        rentRepository.deleteById(rentId);
    }

    public boolean isOwner(UUID rentId, String username) throws ResourceNotFoundException {
        return findById(rentId).getUser().getLogin().equals(username);
    }
}
