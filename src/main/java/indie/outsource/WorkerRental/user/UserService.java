package indie.outsource.WorkerRental.user;

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
            throw new RuntimeException("Resource not found");
    }

    public User findByUsernameExact(String username) {
        if(userRepository.findByLogin(username).isPresent()){
            return userRepository.findByLogin(username).get();
        }
        else
            throw new RuntimeException("Resource not found");
    }

    public List<User> findByUsername(String username) {
        return userRepository.findByLoginContainsIgnoreCase(username);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void activateUser(User user) {
        if(userRepository.findById(user.getId()).isPresent()){
            userRepository.findById(user.getId()).get().setActive(true);
        }
        else
            throw new RuntimeException("Resource not found");
    }

    public void deactivateUser(User user) {
        if(userRepository.findById(user.getId()).isPresent()){
            userRepository.findById(user.getId()).get().setActive(false);
        }
        else
            throw new RuntimeException("Resource not found");
    }
}
