package producers;

import entities.user.UserEnt;
import indie.outsource.event.user.UserEvent;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserProducer {

    private final StreamBridge streamBridge;

    public void sendUserError(UserEvent user) {
        streamBridge.send("removeUser",user);
    }



}
