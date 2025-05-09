package view;

import entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    UserEnt findById(UUID id) throws ResourceNotFoundException;

    UserEnt findByUsernameExact(String username) throws ResourceNotFoundException;

    List<UserEnt> findByUsername(String username);

    List<UserEnt> findAll();

    UserEnt save(UserEnt user) throws UserAlreadyExistsException;



}
