package indie.outsource.WorkerRental.worker;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController()
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/workers/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable @Valid Long id) {
        Worker worker;
        try{
            worker = workerService.findById(id);
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(worker);
    }

    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(workerService.findAll());
    }

    @PostMapping(value = "/workers")
    public ResponseEntity<Worker> createWorker(@RequestBody @Valid Worker worker) {
        return ResponseEntity.ok(workerService.save(worker));
//        return ResponseEntity.created(workerService.save(worker));
    }

    @DeleteMapping("/workers/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable @Valid Long id) {
        try{
            workerService.delete(id);
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (WorkerRentedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

}
