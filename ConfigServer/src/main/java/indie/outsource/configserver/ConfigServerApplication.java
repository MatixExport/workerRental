package indie.outsource.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ConfigServerApplication.class);

        // Add a listener to print the property after the context is ready
        app.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
            Environment env = event.getApplicationContext().getEnvironment();
            String eurekaUri = env.getProperty("eureka.client.service-url.defaultZone");
            System.out.println("Eureka URI: " + eurekaUri);
        });

        app.run(args);
    }

}
