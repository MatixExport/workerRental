package spring.controllers;


import Entities.user.UserEnt;
import indie.outsource.user.ChangePasswordDto;
import indie.outsource.user.SignedCreateUserDTO;
import org.springframework.http.HttpHeaders;
import spring.dtoMappers.UserMapper;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.security.AuthService;
import spring.security.JWSService;
import view.UserService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController()
public class UserController {
    private final UserService userService;
    private final JWSService jwsService;
    private final AuthService authService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserMapper::getUserDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.findById(id)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/users/{id}/signed")
    public ResponseEntity<SignedCreateUserDTO> getSignedUser(@PathVariable UUID id) {
        try{
            SignedCreateUserDTO user = jwsService.signUser(userService.findById(id));
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
        SignedCreateUserDTO user = jwsService.signUser(userService.findByUsername(userDetails.getUsername()).getFirst());
        try {
            return ResponseEntity.ok().eTag(user.getSignature()).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/users/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.findByUsernameExact(login)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/users/loginContains/{login}")
    public ResponseEntity<List<UserDTO>> getUserByLoginContains(@PathVariable String login) {
        return ResponseEntity.ok(userService.findByUsername(login).stream().map(UserMapper::getUserDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN)")
    @PostMapping("/users")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid CreateUserDTO user) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.save(UserMapper.getUser(user))));
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }
    

    @PostMapping("users/self/signed")
    public ResponseEntity<UserDTO> updateUserWithSign(@RequestBody @Valid ChangePasswordDto userDTO, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        System.out.println(ifMatch);
        try {
            if(!jwsService.verifySignedCreateUser(userDTO, ifMatch)){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            if(!authService.verifyPassword(userDTO.getLogin(), userDTO.getOldPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return updateUser(userDTO);
    }


//    @PostMapping("users/self")
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid CreateUserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getDetails();
        UserEnt user = userService.findByUsername(userDetails.getUsername()).getFirst();
        return updateUser(user.getId(), userDTO);
    }
//
//    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.security.Roles).ADMIN)")
//    @PostMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid CreateUserDTO createUserDTO) {
        UserEnt user = UserMapper.getUser(createUserDTO);
        user.setId(id);
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.updateUser(user)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserAlreadyExistsException e) {    //TODO fix exceptions
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN)")
    @PostMapping("/users/{id}/signed")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid SignedCreateUserDTO createUserDTO, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) {
        System.out.println("GOT REQUEST");
        if(!jwsService.verifySignedCreateUser(createUserDTO, ifMatch)){
            System.out.println("VERIFICATION FAILED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return updateUser(id,UserMapper.getCreateUserDTOFromSigned(createUserDTO));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @PostMapping("/users/{id}/activate")
    public ResponseEntity<UserDTO> activateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.activateUser(id)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException(e);      //TODO fix exceptions
        }
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER) ")
    @PostMapping("/users/{id}/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.deactivateUser(id)));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException(e);      //TODO fix exceptions
        }
    }


}
