import Entities.RentEnt;
import Entities.WorkerEnt;
import Entities.user.UserEnt;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WorkerRentedException;
import infrastructure.RentRepository;
import infrastructure.UserRepository;
import infrastructure.WorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import services.RentService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @InjectMocks
    private RentService rentService;

    @Mock
    private  RentRepository rentRepository;

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  WorkerRepository workerRepository;




    @Test
    void createRentTest() throws ResourceNotFoundException, WorkerRentedException, RentAlreadyEndedException, UserInactiveException {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(true);
        Mockito.when(userRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(userEnt));

        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();
        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                        .thenReturn(Optional.of(workerEnt));

        RentEnt rentEnt = new RentEnt(LocalDateTime.now(),workerEnt,userEnt);
        Mockito.when(rentRepository.save(Mockito.any(RentEnt.class)))
                .thenReturn(rentEnt);


        RentEnt createdRent = rentService.createRent(userEnt.getId(),
                workerEnt.getId(),
                rentEnt.getStartDate());

        Assertions.assertEquals(createdRent.getId(),rentEnt.getId());
        Assertions.assertEquals(createdRent.getUser().getLogin(),userEnt.getLogin());
    }

    @Test
    void createRentEmptyUserTest() {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        Mockito.when(userRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->{
            rentService.createRent(userEnt.getId(),
                    UUID.randomUUID(),
                    LocalDateTime.now());
        });
    }

    @Test
    void createRentWorkerAlreadyRentedTest() {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(true);
        Mockito.when(userRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(userEnt));

        WorkerEnt workerEnt = DomainModelFactory.getWorkerEnt();

        Mockito.when(workerRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(workerEnt));

        Mockito.when(rentRepository.existsByWorker_IdAndEndDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(true);

        assertThrows(WorkerRentedException.class,()->{
            rentService.createRent(userEnt.getId(),
                    UUID.randomUUID(),
                    LocalDateTime.now());
        });
    }

    @Test
    void endRentTest() throws ResourceNotFoundException, WorkerRentedException, RentAlreadyEndedException{
        RentEnt rentEnt = DomainModelFactory.getRentEnt();
        rentEnt.setEndDate(null);
        Mockito.when(rentRepository.findById(Mockito.any(UUID.class)))
                        .thenReturn(Optional.of(rentEnt));

        Mockito.when(rentRepository.save(Mockito.any(RentEnt.class)))
                .thenReturn(rentEnt);

        assertDoesNotThrow(()->{
            rentService.endRent(UUID.randomUUID());
        });
    }

    @Test
    void endAlreadyEndedRentTest() {
        RentEnt rentEnt = DomainModelFactory.getRentEnt();
        Mockito.when(rentRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(rentEnt));

        assertThrows(RentAlreadyEndedException.class,()->{
            rentService.endRent(UUID.randomUUID());
        });
    }

}

