package handlers;

import entities.user.UserEnt;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import producers.UserProducer;
import view.UserService;

@Service
@AllArgsConstructor
public class UserCreatedHandler {
    private final UserService userService;
    private final UserProducer userErrorProducer;

    public void handleUserCreated(UserEnt user) {
        try{
            userService.save(user);
        }catch (Exception e){
            userErrorProducer.sendUserError(user);
        }
    }

}
