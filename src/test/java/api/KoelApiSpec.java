package api;
import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import util.ApiTestDataHandler;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;
public class KoelApiSpec {
    public static RequestSpecification requestSpec;
    @BeforeClass
    public void setEnv() {
        Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }
    public static RequestSpecification getAuthRequestSpec() {
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
        return requestSpec = builder.build();
    }
}
