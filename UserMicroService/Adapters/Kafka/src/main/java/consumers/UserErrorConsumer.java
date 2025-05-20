package consumers;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import view.UserService;

import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
@Service
public class UserErrorConsumer {

    private final UserService userService;

    @Bean
    public Consumer<UserEnt> removeUser() {
        return userEnt ->
        {
            try {
                userService.deactivateUser(userEnt.getId());
            } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException(e);
            }
        };
    }
}
