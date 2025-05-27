package services;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import infrastructure.UserEventPort;
import infrastructure.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DomainUserService implements view.UserService {
    private final UserRepository userRepository;
    private final UserEventPort userEventPort;

    public UserEnt findById(UUID id) throws ResourceNotFoundException {
        Optional<UserEnt> userEnt = userRepository.findById(id);
        if(userEnt.isPresent()){
            return userEnt.get();
        }
        else
            throw new ResourceNotFoundException("User with id " + id + " not found");
    }

    public UserEnt findByUsernameExact(String username) throws ResourceNotFoundException {
        Optional<UserEnt> userEnt = userRepository.findByLogin(username);
        if(userEnt.isPresent()){
            return userEnt.get();
        }
        else
            throw new ResourceNotFoundException("User with username " + username + " not found");
    }

    public List<UserEnt> findByUsername(String username) {
        return userRepository.findByLoginContainsIgnoreCase(username);
    }

    public List<UserEnt> findAll() {
        return userRepository.findAll();
    }

    public UserEnt save(UserEnt user) throws UserAlreadyExistsException {
        try{
            if(userRepository.findByLogin(user.getLogin()).isPresent()){
                throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
            }
            return userRepository.save(user);
        }catch(Exception e){
            userEventPort.publishRemoveUserEvent(user);
            throw e;
        }

    }
}
