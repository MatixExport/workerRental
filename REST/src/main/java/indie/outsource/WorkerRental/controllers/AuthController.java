package indie.outsource.WorkerRental.controllers;

import indie.outsource.WorkerRental.services.UserService;
import indie.outsource.user.LoginDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class AuthController {

    UserService userService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody  @Valid LoginDTO loginDTO){
        System.out.println(loginDTO.getLogin());
        return userService.login(loginDTO.getLogin(), loginDTO.getPassword());
    }

}
