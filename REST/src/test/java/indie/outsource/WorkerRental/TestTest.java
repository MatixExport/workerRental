package indie.outsource.WorkerRental;

import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.repositories.UserRepository;
import indie.outsource.WorkerRental.repositories.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTest {
    @Autowired
    WorkerRepository workerRepository;

    @Test
    public void testtest(){
        Worker worker = new Worker();
        worker.setName("test");
        workerRepository.save(worker);
    }
}
