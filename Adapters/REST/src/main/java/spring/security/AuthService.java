package spring.security;

import Entities.user.UserEnt;
import com.nimbusds.jose.JOSEException;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WrongCredentialsException;
import indie.outsource.user.SignedCreateUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.dtoMappers.UserMapper;
import view.UserService;

@Component
@AllArgsConstructor
public class AuthService {

    private JWSUtil jwsUtil;
    private final UserService userService;
    private final AuthHelper authHelper;


    public SignedCreateUserDTO signUser(UserEnt user ) {
        SignedCreateUserDTO signedUserDTO = UserMapper.getSignedUserDTO(user);
        try {
            signedUserDTO.setSignature(jwsUtil.sign(signedUserDTO.getPayload()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return signedUserDTO;
    }

    public Boolean verifySignedCreateUser(SignedCreateUserDTO signedCreateUserDTO, String signature) {
        try {
            if(!jwsUtil.verifySignature(signature,signedCreateUserDTO.getPayload())){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        System.out.println("VERIFICATION OK");
        return true;
    }

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
