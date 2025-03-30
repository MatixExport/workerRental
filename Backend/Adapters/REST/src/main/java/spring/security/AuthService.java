package spring.security;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WrongCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import view.UserService;

@Component
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthHelper authHelper;

    public boolean verifyPassword(String login, String password) throws ResourceNotFoundException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserEnt user = userService.findByUsernameExact(login);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public String login(String login, String password) throws ResourceNotFoundException, WrongCredentialsException, UserInactiveException {
        if(!verifyPassword(login, password)) {
            throw new WrongCredentialsException();
        }
        UserEnt user = userService.findByUsernameExact(login);
        if(!user.isActive()){
            throw new UserInactiveException("User inactive");
        }
        return authHelper.generateJWT(user);

    }
}
