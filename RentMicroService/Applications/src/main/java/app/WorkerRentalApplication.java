package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"app", "view", "spring", "services", "repositories", "infrastructure", "connection", "aggregates","endpoints","configuration","consumers","producers","handlers"}) // TODO fina a way for spring to automatically scan packages from dependencies
public class WorkerRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkerRentalApplication.class, args);
	}

}
