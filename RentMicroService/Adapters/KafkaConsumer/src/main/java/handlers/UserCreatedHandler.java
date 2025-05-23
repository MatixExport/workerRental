package handlers;

import entities.user.UserEnt;
import indie.outsource.event.user.UserEvent;
import lombok.AllArgsConstructor;
import mappers.UserEventMapper;
import org.springframework.stereotype.Service;
import view.UserService;

@Service
@AllArgsConstructor
public class UserCreatedHandler {
    private final UserService userService;

    public void handleUserCreated(UserEvent userEvent) {
        try{
            userService.save(UserEventMapper.toDomainModel(userEvent));
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
