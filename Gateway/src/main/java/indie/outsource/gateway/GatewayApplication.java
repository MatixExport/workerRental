package indie.outsource.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}


	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("*"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.addExposedHeader(HttpHeaders.ETAG);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("worker_service", r -> r.path("/workers/**")
						.uri("lb://worker-rental-rent-microservice"))
				.route("rent_service", r -> r.path("/rents/**")
						.uri("lb://worker-rental-rent-microservice"))
				.route("user_service", r -> r.path("/users/**")
						.uri("lb://worker-rental-user-microservice"))
				.route("login_route", r -> r.path("/login")
						.uri("lb://worker-rental-user-microservice"))
				.route("register_route", r -> r.path("/register")
						.uri("lb://worker-rental-user-microservice"))
				.build();
	}
	}
