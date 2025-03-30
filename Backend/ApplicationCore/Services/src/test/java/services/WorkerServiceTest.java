package services;

import entities.WorkerEnt;
import exceptions.ResourceNotFoundException;
import infrastructure.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @InjectMocks
    private DomainWorkerService workerService;

    @Mock
    private  WorkerRepository workerRepository;


    @Test
    void updateWorkerDoesNotExistTest(){
        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();
        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workerService.updateWorker(workerEnt));
    }

    @Test
    void createWorkerTest() {
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
    void findWorkerDoesNotExistTest() {
        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();
        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workerService.findById(workerEnt.getId()));
    }
}

