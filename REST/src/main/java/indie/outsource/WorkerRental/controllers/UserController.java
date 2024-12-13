package indie.outsource.WorkerRental.controllers;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.exceptions.UserAlreadyExistsException;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.dtoMappers.UserMapper;
import indie.outsource.WorkerRental.services.UserService;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController()
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserMapper::getUserDTO).toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.findById(id)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.findByUsernameExact(login)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @GetMapping("/users/loginContains/{login}")
    public ResponseEntity<List<UserDTO>> getUserByLoginContains(@PathVariable String login) {
        return ResponseEntity.ok(userService.findByUsername(login).stream().map(UserMapper::getUserDTO).toList());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid CreateUserDTO user) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.save(UserMapper.getUser(user))));
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid CreateUserDTO createUserDTO) {
        User user = UserMapper.getUser(createUserDTO);
        user.setId(id);
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.updateUser(user)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/users/{id}/activate")
    public ResponseEntity<UserDTO> activateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.activateUser(id)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/users/{id}/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.deactivateUser(id)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
