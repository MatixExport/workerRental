package indie.outsource.ApplicationCore.repositories.mongoConnection;

import indie.outsource.ApplicationCore.model.Rent;
import indie.outsource.ApplicationCore.model.Worker;
import indie.outsource.ApplicationCore.model.user.Admin;
import indie.outsource.ApplicationCore.model.user.Client;
import indie.outsource.ApplicationCore.model.user.Manager;
import indie.outsource.ApplicationCore.model.user.User;
import indie.outsource.ApplicationCore.repositories.RentRepository;
import indie.outsource.ApplicationCore.repositories.UserRepository;
import indie.outsource.ApplicationCore.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Profile("!test")
public class MongoInitializer implements CommandLineRunner {
    RentRepository rentRepository;

    WorkerRepository workerRepository;

    UserRepository userRepository;

    @Autowired
    public MongoInitializer(RentRepository rentRepository, WorkerRepository workerRepository, UserRepository userRepository) {
        this.rentRepository = rentRepository;
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        rentRepository.deleteAll();
        workerRepository.deleteAll();
        userRepository.deleteAll();

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String password = passwordEncoder.encode("ZAQ!2wsx");
//        User user = new User("User","k1234567",true);
        User client = new Client("Client",password,true);
        User admin = new Admin("Admin",password,true);
        User manager = new Manager("Manager",password,true);

//        user = userRepository.save(user);
        client =userRepository.save(client);
        admin = userRepository.save(admin);
        manager =userRepository.save(manager);

        Worker worker = new Worker("Worker1");
        Worker worker2 = new Worker("Worker2");
        Worker worker3 = new Worker("Worker3");
        Worker worker4 = new Worker("Worker4");
        Worker worker5 = new Worker("Worker5");

        workerRepository.save(worker4);
        workerRepository.save(worker5);

        worker = workerRepository.save(worker);
        worker2 = workerRepository.save(worker2);
        worker3 = workerRepository.save(worker3);

        Rent rent = new Rent(LocalDateTime.now().plusMinutes(2),worker,client);
        Rent rent1 = new Rent(LocalDateTime.now().plusMinutes(1),worker2,client);
        rentRepository.save(rent);
        rentRepository.save(rent1);
    }
}
