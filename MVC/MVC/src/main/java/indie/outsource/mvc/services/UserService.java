package indie.outsource.mvc.services;

import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserService {

    private final WebClient webClient;

    @Autowired
    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }


    public UserDTO createUser(CreateUserDTO createUserDTO) {
        createUserDTO.setType(USERTYPE.CLIENT);
        return webClient.post()
                .uri("/users")
                .bodyValue(createUserDTO)
                .retrieve()
                .bodyToMono(UserDTO.class).onErrorResume(
                        _ ->{
                            throw new RuntimeException("Username already exists");
                        }
                )
                .block();

    }
}
