package indie.outsource.ApplicationCore.controllers;

import indie.outsource.ApplicationCore.exceptions.ResourceNotFoundException;
import indie.outsource.ApplicationCore.exceptions.UserAlreadyExistsException;
import indie.outsource.ApplicationCore.model.user.User;
import indie.outsource.ApplicationCore.dtoMappers.UserMapper;
import indie.outsource.ApplicationCore.services.UserService;
import indie.outsource.user.ChangePasswordDto;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.SignedCreateUserDTO;
import indie.outsource.user.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController()
public class UserController {
    private final UserService userService;

//    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN, T(indie.outsource.WorkerRental.Roles).MANAGER)")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserMapper::getUserDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.findById(id)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN, T(indie.outsource.WorkerRental.Roles).MANAGER)")
    @GetMapping("/users/{id}/signed")
    public ResponseEntity<SignedCreateUserDTO> getSignedUser(@PathVariable UUID id) {
        try{
            SignedCreateUserDTO user = userService.signUser(userService.findById(id));
            return ResponseEntity.ok().eTag(user.getSignature()).body(user);
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
    }

    @GetMapping("/users/self")
    public ResponseEntity<UserDTO> getSelfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getDetails();
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.findByUsername(userDetails.getUsername()).getFirst()));
    }

    @GetMapping("/users/self/signed")
    public ResponseEntity<SignedCreateUserDTO> getSignedSelfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getDetails();
        SignedCreateUserDTO user = userService.signUser(userService.findByUsername(userDetails.getUsername()).getFirst());
        try {
            return ResponseEntity.ok().eTag(user.getSignature()).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @GetMapping("/users/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.findByUsernameExact(login)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @GetMapping("/users/loginContains/{login}")
    public ResponseEntity<List<UserDTO>> getUserByLoginContains(@PathVariable String login) {
        return ResponseEntity.ok(userService.findByUsername(login).stream().map(UserMapper::getUserDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN)")
    @PostMapping("/users")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid CreateUserDTO user) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.save(UserMapper.getUser(user))));
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }

//    @PostMapping("/clients")
//    public ResponseEntity<UserDTO> addClient(@RequestBody @Valid CreateUserDTO Entities.user) {
//        Entities.user.setType(USERTYPE.CLIENT);
//        return addUser(Entities.user);
//    }

    @PostMapping("users/self/signed")
    public ResponseEntity<UserDTO> updateUserWithSign(@RequestBody @Valid ChangePasswordDto userDTO, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        System.out.println(ifMatch);
        try {
            if(!userService.verifySignedCreateUser(userDTO, ifMatch)){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!userService.verifyPassword(userDTO.getLogin(), userDTO.getOldPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return updateUser(userDTO);
    }


//    @PostMapping("users/self")
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid CreateUserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getDetails();
        User user = userService.findByUsername(userDetails.getUsername()).getFirst();
        return updateUser(user.getId(), userDTO);
    }

//    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN)")
//    @PostMapping("/users/{id}")
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

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN)")
    @PostMapping("/users/{id}/signed")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid SignedCreateUserDTO createUserDTO, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        System.out.println("GOT REQUEST");
        try{
            if(!userService.verifySignedCreateUser(createUserDTO, ifMatch)){
                System.out.println("VERIFICATION FAILED");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return updateUser(id,UserMapper.getCreateUserDTOFromSigned(createUserDTO));
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER)")
    @PostMapping("/users/{id}/activate")
    public ResponseEntity<UserDTO> activateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.activateUser(id)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.ApplicationCore.Roles).ADMIN, T(indie.outsource.ApplicationCore.Roles).MANAGER) ")
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
