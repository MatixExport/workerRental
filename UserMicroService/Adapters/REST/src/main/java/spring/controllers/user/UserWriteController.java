package spring.controllers.user;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import indie.outsource.user.ChangePasswordDto;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.SignedCreateUserDTO;
import indie.outsource.user.UserDTO;
import io.micrometer.core.annotation.Timed;
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
import spring.dto.mappers.UserMapper;
import spring.security.AuthService;
import spring.security.JWSService;
import view.UserService;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserWriteController {
    private final UserService userService;
    private final JWSService jwsService;
    private final AuthService authService;


    @Timed("addUser")
    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN)")
    @PostMapping()
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid CreateUserDTO user) {
        try {
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.save(UserMapper.getUser(user))));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @PostMapping("/{id}/activate")
    public ResponseEntity<UserDTO> activateUser(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.activateUser(id)));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER) ")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.deactivateUser(id)));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN)")
    @PostMapping("/{id}/signed")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid SignedCreateUserDTO createUserDTO, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) throws ResourceNotFoundException {
        if (Boolean.FALSE.equals(jwsService.verifySignedCreateUser(createUserDTO, ifMatch))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return updateUser(id, UserMapper.getCreateUserDTOFromSigned(createUserDTO));
    }

    @PostMapping("/self/signed")
    public ResponseEntity<UserDTO> updateUserWithSign(@RequestBody @Valid ChangePasswordDto userDTO, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch) throws ResourceNotFoundException {
        if (Boolean.FALSE.equals(jwsService.verifySignedCreateUser(userDTO, ifMatch))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (!authService.verifyPassword(userDTO.getLogin(), userDTO.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        UserEnt user = userService.findByUsername(userDetails.getUsername()).getFirst();
        return updateUser(user.getId(), userDTO);
    }

    public ResponseEntity<UserDTO> updateUser(UUID id, @RequestBody @Valid CreateUserDTO createUserDTO) throws ResourceNotFoundException {
        UserEnt user = UserMapper.getUser(createUserDTO);
        user.setId(id);
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.updateUser(user)));
    }

}
