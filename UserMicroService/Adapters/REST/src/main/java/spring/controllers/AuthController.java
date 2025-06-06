package spring.controllers;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserInactiveException;
import exceptions.WrongCredentialsException;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.LoginDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.dto.mappers.UserMapper;
import spring.security.AuthService;
import view.UserService;

@AllArgsConstructor
@RestController
public class AuthController {

    UserService userService;
    AuthService authService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody  @Valid LoginDTO loginDTO){
        try{
            return ResponseEntity.ok(authService.login(loginDTO.getLogin(), loginDTO.getPassword()));
        }
        catch (ResourceNotFoundException | WrongCredentialsException em){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        catch (UserInactiveException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Inactive user");
        }
    }

    @Timed(value = "register.post.time", description = "Time taken to register users")
    @Counted(value = "register.post.count", description = "Number of times users were registered")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> addClient(@RequestBody @Valid CreateUserDTO user) {
        user.setType(USERTYPE.CLIENT);
        try{
            UserEnt userEnt = userService.save(UserMapper.getUser(user));
            UserDTO userDTO = UserMapper.getUserDTO(userEnt);

            return ResponseEntity.ok(userDTO);
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }

}
