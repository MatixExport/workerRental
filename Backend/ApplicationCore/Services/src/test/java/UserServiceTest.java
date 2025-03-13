import Entities.user.UserEnt;
import exceptions.*;

import infrastructure.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import services.UserService;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private  UserRepository userRepository;


    @Test
    public void createUserTest() throws UserAlreadyExistsException {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(true);

        Mockito.when(userRepository.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.save(Mockito.any(UserEnt.class)))
                .thenReturn(userEnt);

        UserEnt savedUser = userService.save(userEnt);
        assertEquals(userEnt.getLogin(), savedUser.getLogin());
    }
    @Test
    public void createUserAlreadyExistsTest() throws UserAlreadyExistsException {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(true);

        Mockito.when(userRepository.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.of(userEnt));

        assertThrows(UserAlreadyExistsException.class, () -> userService.save(userEnt));
    }

    @Test
    public void activateUserTest() throws UserAlreadyExistsException {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(false);

        Mockito.when(userRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(userEnt));

        Mockito.when(userRepository.save(Mockito.any(UserEnt.class)))
                .thenReturn(userEnt);

        assertDoesNotThrow(()->{
            userService.activateUser(userEnt.getId());
        });
    }

    @Test
    public void activateUserDoesNotExistTest() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.activateUser(UUID.randomUUID()));
    }







}

