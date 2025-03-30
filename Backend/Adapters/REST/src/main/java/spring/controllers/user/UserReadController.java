package spring.controllers.user;

import com.nimbusds.jose.JOSEException;
import exceptions.ResourceNotFoundException;
import indie.outsource.user.SignedCreateUserDTO;
import indie.outsource.user.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.dto.mappers.UserMapper;
import spring.security.JWSService;
import view.UserService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserReadController {

    private final UserService userService;
    private final JWSService jwsService;


    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserMapper::getUserDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.findById(id)));
    }

    @GetMapping("/{id}/signed")
    public ResponseEntity<SignedCreateUserDTO> getSignedUser(@PathVariable UUID id) throws ResourceNotFoundException {
        try {
            SignedCreateUserDTO user = jwsService.signUser(userService.findById(id));
            return ResponseEntity.ok().eTag(user.getSignature()).body(user);
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/login/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login) throws ResourceNotFoundException {
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.findByUsernameExact(login)));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/loginContains/{login}")
    public ResponseEntity<List<UserDTO>> getUserByLoginContains(@PathVariable String login) {
        return ResponseEntity.ok(userService.findByUsername(login).stream().map(UserMapper::getUserDTO).toList());
    }

    @GetMapping("/users/self")
    public ResponseEntity<UserDTO> getSelfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.findByUsername(userDetails.getUsername()).getFirst()));
    }

    @GetMapping("/users/self/signed")
    public ResponseEntity<SignedCreateUserDTO> getSignedSelfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        try {
            SignedCreateUserDTO user = jwsService.signUser(userService.findByUsername(userDetails.getUsername()).getFirst());
            return ResponseEntity.ok().eTag(user.getSignature()).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
    }
}
