package indie.outsource.WorkerRental.worker;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.user.User;
import indie.outsource.WorkerRental.user.UserMapper;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController()
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> getWorker(@PathVariable UUID id) {
        Worker worker;
        try{
            return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.findById(id)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/workers")
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        return ResponseEntity.ok(workerService.findAll().stream().map(WorkerMapper::getWorkerDto).toList());
    }

    @PostMapping(value = "/workers")
    public ResponseEntity<WorkerDTO> createWorker(@RequestBody @Valid CreateWorkerDTO worker) {
        return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.save(WorkerMapper.getWorker(worker))));
    }

    @PostMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid CreateWorkerDTO createWorkerDTO) {
        Worker worker = WorkerMapper.getWorker(createWorkerDTO);
        worker.setId(id);
        try{
            return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.save(worker)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/workers/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable @Valid UUID id) {
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
