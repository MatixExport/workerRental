package services;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private DomainUserService userService;

    @Mock
    private  UserRepository userRepository;


    @Test
    void createUserTest() throws UserAlreadyExistsException {
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
    void createUserAlreadyExistsTest() {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(true);

        Mockito.when(userRepository.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.of(userEnt));

        assertThrows(UserAlreadyExistsException.class, () -> userService.save(userEnt));
    }

    @Test
    void activateUserTest() {
        UserEnt userEnt = DomainModelFactory.getAdminEnt();
        userEnt.setActive(false);

        Mockito.when(userRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(userEnt));

        Mockito.when(userRepository.updateUser(Mockito.any(UserEnt.class)))
                .thenReturn(userEnt);

        assertDoesNotThrow(()->{
            userService.activateUser(userEnt.getId());
        });
    }

    @Test
    void activateUserDoesNotExistTest() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.activateUser(UUID.randomUUID()));
    }







}

