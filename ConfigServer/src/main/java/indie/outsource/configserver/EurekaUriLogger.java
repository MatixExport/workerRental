package indie.outsource.configserver;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EurekaUriLogger {

    @Value("${eureka.client.service-url.default.zone}")
    private String eurekaUri;

    @PostConstruct
    public void logEurekaUri() {
        System.out.println("Resolved Eureka URI: " + eurekaUri);
    }
}
