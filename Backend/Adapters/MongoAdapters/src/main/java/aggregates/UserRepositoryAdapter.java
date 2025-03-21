package aggregates;

import Entities.user.UserEnt;
import aggregates.mappers.UserMapper;
import documents.users.UserMgd;
import exceptions.UserAlreadyExistsException;
import infrastructure.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import repositories.interfaces.MongoUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final MongoUserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserEnt> findAll() {
        return userMapper.toDomainModel(userRepository.findAll());
    }

    @Override
    public Optional<UserEnt> findById(UUID id) {
        UserMgd userMgd = userRepository.findById(id);
        if (userMgd != null) {return Optional.of(userMapper.toDomainModel(userMgd));}
        return Optional.empty();
    }

    @Override
    public UserEnt save(UserEnt user) throws UserAlreadyExistsException {
        return userMapper.toDomainModel(userRepository.save(userMapper.toMongoModel(user)));
    }

    @Override
    public UserEnt updateUser(UserEnt user) {
        return userMapper.toDomainModel(userRepository.update(userMapper.toMongoModel(user)));
    }

    @Override
    public void delete(UserEnt user) {
        userRepository.delete(userMapper.toMongoModel(user));
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public Optional<UserEnt> findByLogin(String login) {
        UserMgd userMgd = userRepository.findByLogin(login);
        if (userMgd != null) {
            return Optional.of(userMapper.toDomainModel(userMgd));
        }
        return Optional.empty();
    }

    @Override
    public List<UserEnt> findByLoginContainsIgnoreCase(String login) {
        return userMapper.toDomainModel(userRepository.findByLoginContainsIgnoreCase(login));
    }
}
