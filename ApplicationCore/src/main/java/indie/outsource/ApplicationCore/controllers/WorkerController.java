package indie.outsource.ApplicationCore.controllers;

import indie.outsource.ApplicationCore.exceptions.ResourceNotFoundException;
import indie.outsource.ApplicationCore.model.Worker;
import indie.outsource.ApplicationCore.dtoMappers.WorkerMapper;
import indie.outsource.ApplicationCore.exceptions.WorkerRentedException;
import indie.outsource.ApplicationCore.services.WorkerService;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController()
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> getWorker(@PathVariable UUID id) {
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

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @PostMapping(value = "/workers")
    public ResponseEntity<WorkerDTO> createWorker(@RequestBody @Valid CreateWorkerDTO worker) {
        return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.save(WorkerMapper.getWorker(worker))));
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @PostMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> updateWorker(@PathVariable UUID id, @RequestBody @Valid CreateWorkerDTO createWorkerDTO) {
        Worker worker = WorkerMapper.getWorker(createWorkerDTO);
        worker.setId(id);
        try{
            return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.updateWorker(worker)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @DeleteMapping("/workers/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable @Valid UUID id) {
        try{
            workerService.delete(id);
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (WorkerRentedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }

}
