package view;

import Entities.user.UserEnt;
import exceptions.ResourceNotFoundException;
import exceptions.UserAlreadyExistsException;
import jdk.jshell.spi.ExecutionControl;
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

    UserEnt updateUser(UserEnt user) throws ResourceNotFoundException, UserAlreadyExistsException;

    UserEnt activateUser(UUID id) throws ResourceNotFoundException, UserAlreadyExistsException;

    UserEnt deactivateUser(UUID id) throws ResourceNotFoundException, UserAlreadyExistsException;


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
