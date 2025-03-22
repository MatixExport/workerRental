package spring.controllers;


import Entities.WorkerEnt;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import indie.outsource.worker.CreateWorkerDTO;
import indie.outsource.worker.WorkerDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.dtoMappers.WorkerMapper;
import view.WorkerService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController()
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> getWorker(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.findById(id)));
    }

    @GetMapping("/workers")
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        return ResponseEntity.ok(workerService.findAll().stream().map(WorkerMapper::getWorkerDto).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @PostMapping(value = "/workers")
    public ResponseEntity<WorkerDTO> createWorker(@RequestBody @Valid CreateWorkerDTO worker) {
        return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.save(WorkerMapper.getWorker(worker))));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @PostMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> updateWorker(@PathVariable UUID id, @RequestBody @Valid CreateWorkerDTO createWorkerDTO) throws ResourceNotFoundException {
        WorkerEnt worker = WorkerMapper.getWorker(createWorkerDTO);
        worker.setId(id);
        return ResponseEntity.ok(WorkerMapper.getWorkerDto(workerService.updateWorker(worker)));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @DeleteMapping("/workers/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable @Valid UUID id) throws ResourceNotFoundException, WorkerRentedException {
        workerService.delete(id);
        return ResponseEntity.ok().build();
    }

}
