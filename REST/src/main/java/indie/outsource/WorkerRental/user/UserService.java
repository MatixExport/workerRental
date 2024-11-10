package indie.outsource.WorkerRental.user;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {
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
        return (List<User>) userRepository.findAll();
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

    public User activateUser(Long id) {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        User user = userRepository.findById(id).get();
        user.setActive(true);
        return user;
    }

    public User deactivateUser(Long id) {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        User user = userRepository.findById(id).get();
        user.setActive(false);
        return user;
    }
}
