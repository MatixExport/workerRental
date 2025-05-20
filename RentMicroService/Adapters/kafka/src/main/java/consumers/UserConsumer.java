package consumers;

import entities.user.UserEnt;
import handlers.UserCreatedHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import view.UserService;

import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
@Service
public class UserConsumer {

    private final UserCreatedHandler userCreatedHandler;


    @Bean
    public Consumer<UserEnt> input() {
        return userCreatedHandler::handleUserCreated;
    }
}
