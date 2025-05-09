package spring.controllers.user;

import exceptions.UserAlreadyExistsException;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.dto.mappers.UserMapper;
import view.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserWriteController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN)")
    @PostMapping()
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid CreateUserDTO user) {
        try {
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.save(UserMapper.getUser(user))));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }
}
