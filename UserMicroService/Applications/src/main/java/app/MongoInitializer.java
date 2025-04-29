package app;

import entities.user.AdminEnt;
import entities.user.ClientEnt;
import entities.user.ManagerEnt;
import entities.user.UserEnt;
import infrastructure.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!test")
@AllArgsConstructor
public class MongoInitializer implements CommandLineRunner {


    UserRepository userRepository;



    @Override
    public void run(String... args) throws Exception {
       userRepository.deleteAll();

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String mockPassword = passwordEncoder.encode("ZAQ!2wsx");
        UserEnt client = new ClientEnt("Client",mockPassword,true);
        UserEnt admin = new AdminEnt("Admin",mockPassword,true);
        UserEnt manager = new ManagerEnt("Manager",mockPassword,true);

        userRepository.save(client);
        userRepository.save(admin);
        userRepository.save(manager);
    }
}
