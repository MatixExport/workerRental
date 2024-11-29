package indie.outsource.WorkerRental.repositories.mongoConnection;

import indie.outsource.WorkerRental.model.Rent;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.model.user.Admin;
import indie.outsource.WorkerRental.model.user.Client;
import indie.outsource.WorkerRental.model.user.Manager;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.repositories.RentRepository;
import indie.outsource.WorkerRental.repositories.UserRepository;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

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

//        User user = new User("User","k1234567",true);
        User client = new Client("Client","k1234567",true);
        User admin = new Admin("Admin","k1234567",true);
        User manager = new Manager("Manager","k1234567",true);

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
