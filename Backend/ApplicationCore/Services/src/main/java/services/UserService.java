package services;

import Entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import infrastructure.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService implements view.UserService {
    private final UserRepository userRepository;
//    private final AuthHelper authHelper;
//    private final JWSUtil jwsUtil;

    public UserEnt findById(UUID id) throws ResourceNotFoundException {
        if(userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        else
            throw new ResourceNotFoundException("User with id " + id + " not found");
    }

    public UserEnt findByUsernameExact(String username) throws ResourceNotFoundException {
        if(userRepository.findByLogin(username).isPresent()){
            return userRepository.findByLogin(username).get();
        }
        else
            throw new ResourceNotFoundException("User with username " + username + " not found");
    }

    public List<UserEnt> findByUsername(String username) {
        return userRepository.findByLoginContainsIgnoreCase(username);
    }

    public List<UserEnt> findAll() {
        return userRepository.findAll();
    }

    public UserEnt save(UserEnt user) throws UserAlreadyExistsException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(userRepository.findByLogin(user.getLogin()).isPresent()){
            throw new UserAlreadyExistsException("User with login " + user.getLogin() + " already exists");
        }
        return userRepository.save(user);
    }

    public UserEnt updateUser(UserEnt user) throws ResourceNotFoundException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<UserEnt> getUser = userRepository.findById(user.getId());
        if (getUser.isEmpty()){
            throw new ResourceNotFoundException("User with id " + user.getId() + " not found");
        }
        user.setActive(getUser.get().isActive());
        return userRepository.updateUser(user);
    }

    public UserEnt activateUser(UUID id) throws ResourceNotFoundException {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        UserEnt user = userRepository.findById(id).get();
        user.setActive(true);
        userRepository.updateUser(user);
        return user;
    }

    public UserEnt deactivateUser(UUID id) throws ResourceNotFoundException {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        UserEnt user = userRepository.findById(id).get();
        user.setActive(false);
        userRepository.updateUser(user);
        return user;
    }

//    public SignedCreateUserDTO signUser(UserEnt user ) {
//        SignedCreateUserDTO signedUserDTO = UserMapper.getSignedUserDTO(user);
//        try {
//            signedUserDTO.setSignature(jwsUtil.sign(signedUserDTO.getPayload()));
//        } catch (JOSEException e) {
//            throw new RuntimeException(e);
//        }
//        return signedUserDTO;
//    }
//
//    public Boolean verifySignedCreateUser(SignedCreateUserDTO signedCreateUserDTO, String signature) {
//        try {
//
//            if(!jwsUtil.verifySignature(signature,signedCreateUserDTO.getPayload())){
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        System.out.println("VERIFICATION OK");
//        return true;
//    }
//
//    public boolean verifyPassword(String login, String password){
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//        Optional<UserEnt> user = userRepository.findByLogin(login);
//        if(user.isPresent()){
//            return passwordEncoder.matches(password, user.get().getPassword());
//        }
//        throw new ResourceNotFoundException("User with login " + login + " not found");
//    }
//
//    public String login(String login, String password) {
//        if(verifyPassword(login, password)){
//            Optional<UserEnt> user = userRepository.findByLogin(login);
//                if(user.get().isActive()){
//                    return authHelper.generateJWT(user.get());
//                }
//                throw new UserInactiveException("User inactive");
//        }
//        throw new WrongCredentialsException();
//    }
}
