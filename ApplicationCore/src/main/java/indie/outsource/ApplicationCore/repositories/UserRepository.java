package indie.outsource.ApplicationCore.repositories;

import indie.outsource.ApplicationCore.model.user.User;
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
