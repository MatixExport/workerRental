package indie.outsource.WorkerRental.worker;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController()
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/worker/{id}")
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

    @GetMapping("/worker")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        System.out.println("hi");
        return ResponseEntity.ok(workerService.findAll());
    }

    @PostMapping(value = "/worker")
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) {
        return ResponseEntity.ok(workerService.save(worker));
    }

    @DeleteMapping("/worker/{id}")
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
