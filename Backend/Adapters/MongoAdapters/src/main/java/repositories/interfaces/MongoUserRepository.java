package repositories.interfaces;

import documents.users.UserMgd;
import exceptions.UserAlreadyExistsException;

import java.util.List;
import java.util.UUID;


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
