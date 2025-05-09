package spring.controllers.user;

import exceptions.ResourceNotFoundException;
import indie.outsource.user.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.dto.mappers.UserMapper;
import view.UserService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserReadController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserMapper::getUserDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN, T(spring.security.Roles).MANAGER)")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(UserMapper.getUserDTO(userService.findById(id)));
    }

}
