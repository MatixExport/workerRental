package producers;

import entities.user.UserEnt;
import infrastructure.UserEventPort;
import lombok.AllArgsConstructor;
import mappers.UserEventMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserEventPortAdatper implements UserEventPort{

    private final UserProducer producer;

    @Override
    public void publishRemoveUserEvent(UserEnt userEnt) {
        producer.sendUserError(UserEventMapper.fromDomainModel(userEnt));
    }
}
