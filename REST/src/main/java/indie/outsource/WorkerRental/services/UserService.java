package indie.outsource.WorkerRental.services;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.exceptions.UserAlreadyExistsException;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if(userRepository.findByLogin(user.getLogin()).isPresent()){
            throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user){
        if (userRepository.findById(user.getId()).isEmpty()){
            throw new ResourceNotFoundException("User with id " + user.getId() + " not found");
        }
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


}
