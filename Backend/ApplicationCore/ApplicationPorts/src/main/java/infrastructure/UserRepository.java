package infrastructure;

import entities.user.UserEnt;
import exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository{
    List<UserEnt> findAll();
    Optional<UserEnt> findById(UUID id);
    UserEnt save(UserEnt user) throws UserAlreadyExistsException;

    UserEnt updateUser(UserEnt user);
    void delete(UserEnt user);
    void deleteById(UUID id);
    void deleteAll();

    Optional<UserEnt> findByLogin(String login);
    List<UserEnt> findByLoginContainsIgnoreCase(String login);
}
