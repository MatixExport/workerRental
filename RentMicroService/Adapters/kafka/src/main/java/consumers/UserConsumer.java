package consumers;

import entities.user.UserEnt;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserConsumer {

    @Bean
    public Consumer<UserEnt> input(){
        return System.out::println;
    }
}
