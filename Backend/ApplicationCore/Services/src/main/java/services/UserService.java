package services;

import Entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
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
public class UserService implements view.UserService {
    private final UserRepository userRepository;

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
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(userRepository.findByLogin(user.getLogin()).isPresent()){
            throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
        }
        return userRepository.save(user);
    }

    public UserEnt updateUser(UserEnt user) throws ResourceNotFoundException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<UserEnt> getUser = userRepository.findById(user.getId());
        if (getUser.isEmpty()){
            throw new ResourceNotFoundException("User with id " + user.getId() + " not found");
        }
        user.setActive(getUser.get().isActive());
        return userRepository.updateUser(user);
    }

    public UserEnt activateUser(UUID id) throws ResourceNotFoundException {
        Optional<UserEnt> userEnt = userRepository.findById(id);
        if(userEnt.isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        UserEnt user = userEnt.get();
        user.setActive(true);
        userRepository.updateUser(user);
        return user;
    }

    public UserEnt deactivateUser(UUID id) throws ResourceNotFoundException {
        Optional<UserEnt> userEnt = userRepository.findById(id);
        if(userEnt.isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        UserEnt user = userEnt.get();
        user.setActive(false);
        userRepository.updateUser(user);
        return user;
    }
}
