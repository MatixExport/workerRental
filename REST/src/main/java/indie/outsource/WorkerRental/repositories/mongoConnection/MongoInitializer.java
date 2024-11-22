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
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;


import java.time.LocalDateTime;


@ApplicationScoped
@IfBuildProfile("prod")
public class MongoInitializer {
    @Inject
    RentRepository rentRepository;

    @Inject
    WorkerRepository workerRepository;

    @Inject
    UserRepository userRepository;


    public void onStart(@Observes StartupEvent event) {
        rentRepository.deleteAll();
        workerRepository.deleteAll();
        userRepository.deleteAll();

        User client = new Client("Client","k1234567",true);
        User admin = new Admin("Admin","k1234567",true);
        User manager = new Manager("Manager","k1234567",true);

        client =userRepository.save(client);
        admin = userRepository.save(admin);
        manager =userRepository.save(manager);

        Worker worker = new Worker("Worker1");
        Worker worker2 = new Worker("Worker2");
        Worker worker3 = new Worker("Worker3");

        worker = workerRepository.save(worker);
        worker2 = workerRepository.save(worker2);
        worker3 = workerRepository.save(worker3);

        Rent rent = new Rent(LocalDateTime.now().plusMinutes(2),worker,client);
        Rent rent1 = new Rent(LocalDateTime.now().plusMinutes(1),worker2,client);
        rentRepository.save(rent);
        rentRepository.save(rent1);
    }
}
