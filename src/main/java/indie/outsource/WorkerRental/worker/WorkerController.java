package indie.outsource.WorkerRental.worker;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController()
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/workers/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable Long id) {
        Worker worker;
        try{
            worker = workerService.findById(id);
        }
        catch(Exception e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(worker);
    }

    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        System.out.println("hi");
        return ResponseEntity.ok(workerService.findAll());
    }

    @PostMapping(value = "/workers")
    public ResponseEntity<Worker> createWorker(@RequestBody @Valid Worker worker) {
        return ResponseEntity.ok(workerService.save(worker));
//        return ResponseEntity.created(workerService.save(worker));
    }

    @DeleteMapping("/workers/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable Long id) {
        try{
            workerService.delete(id);
        }
        catch(Exception e){
            if(e.getMessage().equals("Resource not found")){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
