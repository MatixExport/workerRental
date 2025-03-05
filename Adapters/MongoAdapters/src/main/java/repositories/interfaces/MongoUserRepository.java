package repositories.interfaces;

import documents.user.UserMgd;
import exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Repository
public interface MongoUserRepository {

    List<UserMgd> findAll();
    UserMgd findById(UUID id);
    UserMgd save(UserMgd user) throws UserAlreadyExistsException;
    void delete(UserMgd user);
    void deleteById(UUID id);
    void deleteAll();

    UserMgd findByLogin(String login);
    List<UserMgd> findByLoginContainsIgnoreCase(String login);
}
