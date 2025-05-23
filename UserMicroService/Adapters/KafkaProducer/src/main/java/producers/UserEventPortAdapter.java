package producers;

import entities.user.UserEnt;
import infrastructure.UserEventPort;
import lombok.AllArgsConstructor;
import mappers.UserEventMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserEventPortAdapter implements UserEventPort{

    private final UserProducer producer;

    @Override
    public void publishCreateUserEvent(UserEnt userEnt) {
        producer.sendUser(UserEventMapper.fromDomainModel(userEnt));
    }
}
