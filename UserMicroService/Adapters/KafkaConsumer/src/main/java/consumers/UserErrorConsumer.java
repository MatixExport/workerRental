package consumers;

import exceptions.ResourceNotFoundException;
import indie.outsource.event.user.UserEvent;
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
    public Consumer<UserEvent> removeUser() {
        return userEvent ->
        {
            try {
                userService.deactivateUser(userEvent.id());
            } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException(e);
            }
        };
    }
}
