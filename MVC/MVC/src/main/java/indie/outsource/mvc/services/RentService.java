package indie.outsource.mvc.services;

import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.FullCreateRentDTO;
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

    public List<RentDTO> getAllRents() {
        return webClient.get()
                .uri("/rents")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RentDTO>>() {})
                .block();
    }

    public void endRent(UUID rentId) {
        webClient.post()
                .uri("/rents/"+rentId+"/finish")
                .retrieve()
                .onStatus((status)->status.value()==409,_->{
                    throw new RuntimeException("Rent already ended");
                })
                .onStatus((status)->status.value()==404,_->{
                    throw new RuntimeException("Rent with provided id does not exist");
                })
                .onStatus(HttpStatusCode::isError,(clientResponse -> {
                    throw new RuntimeException(clientResponse.statusCode().toString());
                })).toBodilessEntity().block();
    }


    public List<WorkerDTO> getAllWorkers() {
         return webClient.get()
                 .uri("/workers")
                 .retrieve()
                 .bodyToMono(new ParameterizedTypeReference<List<WorkerDTO>>() {})
                 .block();
    }

    public RentDTO createRent(FullCreateRentDTO createRentDTO) {
        return  webClient.post()
                .uri("/rents/users/" + createRentDTO.getUserID() + "/workers/" +createRentDTO.getWorkerID())
                .bodyValue(createRentDTO)
                .retrieve()
                .onStatus(status -> status.value() == 409, _->{
                    throw new RuntimeException("Worker is already rented");
                })
                .onStatus((status)->status.value()==403,_->{
                    throw new RuntimeException("User is inactive");
                })
                .onStatus(HttpStatusCode::is4xxClientError,(clientResponse -> {
                    return clientResponse.createException().flatMap(Mono::error);
                }))
                .bodyToMono(RentDTO.class)
                .block();

    }




}
