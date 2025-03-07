package spring.controllers;

import spring.dtoMappers.UserMapper;
import exceptions.UserAlreadyExistsException;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import view.UserService;

@AllArgsConstructor
@RestController
public class AuthController {

    UserService userService;

//    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> login(@RequestBody  @Valid LoginDTO loginDTO){
//        try{
//            return ResponseEntity.ok(userService.login(loginDTO.getLogin(), loginDTO.getPassword()));
//        }
//        catch (ResourceNotFoundException | WrongCredentialsException em){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//        catch (UserInactiveException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Inactive user");
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> addClient(@RequestBody @Valid CreateUserDTO user) {
        user.setType(USERTYPE.CLIENT);
        try{
            return ResponseEntity.ok(UserMapper.getUserDTO(userService.save(UserMapper.getUser(user))));
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMapper.getUserDTO(UserMapper.getUser(user)));
        }
    }

}
