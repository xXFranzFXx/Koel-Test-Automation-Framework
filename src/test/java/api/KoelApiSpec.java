package api;
import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.playlist.Playlist;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class KoelApiSpec {

    @BeforeClass
    public void setEnv() {
        Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }

    public static RequestSpecification getAuthRequestSpec() {
        RequestSpecification requestSpec;
        Response response = given()
                .params("email", System.getProperty("koelUser"),
                        "password", System.getProperty("koelPassword"))
                .baseUri("https://qa.koel.app/api/me")
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        String accessToken = response.path("token");
        String Authorization = "Bearer " + accessToken;

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", Authorization);
        requestSpec = builder.build();
        return requestSpec;
    }

}
