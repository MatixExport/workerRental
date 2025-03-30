package view;


import entities.RentEnt;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WorkerRentedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public interface RentService {

    RentEnt findById(UUID id) throws ResourceNotFoundException;

    List<RentEnt> findAll();

    RentEnt createRent(UUID clientId, UUID workerId, LocalDateTime startDate) throws ResourceNotFoundException, UserInactiveException, WorkerRentedException;

    List<RentEnt> getClientActiveRents(UUID clientId) throws ResourceNotFoundException;

    List<RentEnt> getClientEndedRents(UUID clientId) throws ResourceNotFoundException;

    List<RentEnt> getWorkerActiveRents(UUID workerId) throws ResourceNotFoundException;

    List<RentEnt> getWorkerEndedRents(UUID workerId) throws ResourceNotFoundException;

    RentEnt endRent(UUID rentId) throws ResourceNotFoundException, RentAlreadyEndedException;

    void deleteRent(UUID rentId) throws ResourceNotFoundException, RentAlreadyEndedException;

    boolean isOwner(UUID rentId, String username) throws ResourceNotFoundException;
}
