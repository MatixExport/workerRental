package app.helper;

import indie.outsource.user.LoginDTO;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

public class RestRequests {
    private int port;
    private String baseUri;
    private RestAssuredConfig rac;

    public RestRequests(Integer port, String baseUri, RestAssuredConfig rac) {
        System.out.println(port + " " + baseUri);
        this.port = port;
        this.baseUri = baseUri;
        this.rac = rac;
    }

    public String getValidJwtForUser(String login, String password) {
        return RestAssured.given()
                .port(port)
                .config(rac)
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(new LoginDTO(login, password))
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();
    }
}
