package indie.outsource.WorkerRental.repositories;

import indie.outsource.WorkerRental.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByLogin(String login);
    List<User> findByLoginContainsIgnoreCase(String login);
}
