package app;

import entities.RentEnt;
import entities.WorkerEnt;
import entities.user.UserEnt;
import infrastructure.RentRepository;
import infrastructure.UserRepository;
import infrastructure.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Profile("!test")
@AllArgsConstructor
public class MongoInitializer implements CommandLineRunner {
    RentRepository rentRepository;

    WorkerRepository workerRepository;

    UserRepository userRepository;



    @Override
    public void run(String... args) throws Exception {
        rentRepository.deleteAll();
        workerRepository.deleteAll();
        userRepository.deleteAll();

        UserEnt client = new UserEnt("Client");
        UserEnt admin = new UserEnt("Admin");
        UserEnt manager = new UserEnt("Manager");

        client =userRepository.save(client);
        userRepository.save(admin);
        userRepository.save(manager);

        WorkerEnt worker = new WorkerEnt("Worker1");
        WorkerEnt worker2 = new WorkerEnt("Worker2");
        WorkerEnt worker3 = new WorkerEnt("Worker3");
        WorkerEnt worker4 = new WorkerEnt("Worker4");
        WorkerEnt worker5 = new WorkerEnt("Worker5");

        workerRepository.save(worker4);
        workerRepository.save(worker5);

        worker = workerRepository.save(worker);
        worker2 = workerRepository.save(worker2);
        workerRepository.save(worker3);

        RentEnt rent = new RentEnt(LocalDateTime.now().plusMinutes(2),worker,client);
        RentEnt rent1 = new RentEnt(LocalDateTime.now().plusMinutes(1),worker2,client);
        rentRepository.save(rent);
        rentRepository.save(rent1);
    }
}
