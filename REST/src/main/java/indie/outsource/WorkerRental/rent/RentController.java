package indie.outsource.WorkerRental.rent;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.user.UserInactiveException;
import indie.outsource.WorkerRental.worker.WorkerRentedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController()
public class RentController {
    private RentService rentService;

    @GetMapping("/rents/{id}")
    public ResponseEntity<Rent> getRent(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(rentService.findById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/rents/users/{clientId}/workers/{workerId}")
    public ResponseEntity<Rent> createRent(@PathVariable UUID clientId, @PathVariable UUID workerId, @RequestBody Rent rent) {
        try {
            return ResponseEntity.ok(rentService.createRent(clientId, workerId, rent.startDate));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserInactiveException | WorkerRentedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/rents/{id}/finish")
    public ResponseEntity<Rent> finishRent(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(rentService.endRent(id));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (RentAlreadyEndedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/rents/{id}/delete")
    public ResponseEntity<String> deleteRent(@PathVariable UUID id) {
        try {
            rentService.deleteRent(id);
            return ResponseEntity.ok("");
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (RentNotEndedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/rents/ended/users/{id}")
    public ResponseEntity<List<Rent>> getClientEndedRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getClientEndedRents(id));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rents/ended/workers/{id}")
    public ResponseEntity<List<Rent>> getWorkerEndedRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getWorkerEndedRents(id));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rents/current/users/{id}")
    public ResponseEntity<List<Rent>> getClientActiveRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getClientActiveRents(id));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rents/current/workers/{id}")
    public ResponseEntity<List<Rent>> getWorkerActiveRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getWorkerActiveRents(id));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
