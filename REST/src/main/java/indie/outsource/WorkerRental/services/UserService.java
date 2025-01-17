package indie.outsource.WorkerRental.services;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.exceptions.UserAlreadyExistsException;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findById(UUID id) {
        if(userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        else
            throw new ResourceNotFoundException("User with id " + id + " not found");
    }

    public User findByUsernameExact(String username) {
        if(userRepository.findByLogin(username).isPresent()){
            return userRepository.findByLogin(username).get();
        }
        else
            throw new ResourceNotFoundException("User with username " + username + " not found");
    }

    public List<User> findByUsername(String username) {
        return userRepository.findByLoginContainsIgnoreCase(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(userRepository.findByLogin(user.getLogin()).isPresent()){
            throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<User> getUser = userRepository.findById(user.getId());
        if (getUser.isEmpty()){
            throw new ResourceNotFoundException("User with id " + user.getId() + " not found");
        }
        user.setActive(getUser.get().isActive());
        return userRepository.save(user);
    }

    public User activateUser(UUID id) {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        User user = userRepository.findById(id).get();
        user.setActive(true);
        userRepository.save(user);
        return user;
    }

    public User deactivateUser(UUID id) {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        User user = userRepository.findById(id).get();
        user.setActive(false);
        userRepository.save(user);
        return user;
    }


    public String login(String login, String password) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Optional<User> user = userRepository.findByLogin(login);
        if(user.isPresent()){
            if(passwordEncoder.matches(password, user.get().getPassword())){
                return "logged in successfully";
            }
        }
        throw new ResourceNotFoundException("User with login " + login + " not found");
    }
}
