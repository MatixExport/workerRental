package indie.outsource.mvc.services;

import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import indie.outsource.worker.WorkerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class RentService {

    private final WebClient webClient;

    @Autowired
    public RentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public List<WorkerDTO> getAllWorkers() {
         return webClient.get()
                 .uri("/workers")
                 .retrieve()
                 .bodyToMono(new ParameterizedTypeReference<List<WorkerDTO>>() {})
                 .block();
    }

    public RentDTO createRent(CreateRentDTO createRentDTO, UUID clientId,UUID workerId) {
        return  webClient.post()
                .uri("/rents/users/" + clientId + "/workers/" + workerId)
                .bodyValue(createRentDTO)
                .retrieve()
                .onStatus(status -> status.value() == 409,_->{
                    throw new RuntimeException("Worker is already rented");
                })
                .onStatus(HttpStatusCode::is4xxClientError,(clientResponse -> {
                    return clientResponse.createException().flatMap(Mono::error);
                }))
                .bodyToMono(RentDTO.class)
                .block();

    }




}
