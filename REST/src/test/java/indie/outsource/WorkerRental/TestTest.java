package indie.outsource.WorkerRental;


import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.repositories.RentRepository;
import indie.outsource.WorkerRental.repositories.UserRepository;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class TestTest {
    @Autowired
    UserRepository userRepository;


    @Test
    public void testtest(){


        User user = new User();
        user.setActive(true);
        user.setLogin("test");
        user.setPassword("test");

        user = userRepository.save(user);
        user.setActive(false);
        user = userRepository.save(user);
        user = userRepository.findById(user.getId()).get();
        Assertions.assertFalse(user.isActive());
    }
}
