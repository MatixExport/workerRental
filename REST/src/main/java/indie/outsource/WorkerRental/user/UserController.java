package indie.outsource.WorkerRental.user;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController()
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/users/login/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        try{
            return ResponseEntity.ok(userService.findByUsernameExact(login));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/loginContains/{login}")
    public ResponseEntity<List<User>> getUserByLoginContains(@PathVariable String login) {
        return ResponseEntity.ok(userService.findByUsername(login));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try{
            return ResponseEntity.ok(userService.save(user));
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        //user.setId
        try{
            return ResponseEntity.ok(userService.updateUser(user));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/users/{id}/activate")
    public ResponseEntity<User> activateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(userService.activateUser(id));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/users/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(userService.deactivateUser(id));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
