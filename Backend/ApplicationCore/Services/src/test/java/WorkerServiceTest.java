import Entities.WorkerEnt;
import exceptions.*;

import infrastructure.WorkerRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import services.WorkerService;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WorkerServiceTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private  WorkerRepository workerRepository;


    @Test
    public void updateWorkerDoesNotExistTest(){
        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();
        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workerService.updateWorker(workerEnt));
    }

    @Test
    public void createWorkerTest() {
        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();

        Mockito.when(workerRepository.save(Mockito.any(WorkerEnt.class)))
                .thenReturn(workerEnt);

        WorkerEnt savedWorker = workerService.save(workerEnt);
        assertEquals(savedWorker.getId(), workerEnt.getId());
    }
    @Test
    void findWorkerTest() throws ResourceNotFoundException {
        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();
        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(workerEnt));

        WorkerEnt savedWorker = workerService.findById(workerEnt.getId());
        assertEquals(savedWorker.getId(), workerEnt.getId());
    }

    @Test
    void findWorkerDoesNotExistTest() throws ResourceNotFoundException {
        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();
        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workerService.findById(workerEnt.getId()));
    }
}

