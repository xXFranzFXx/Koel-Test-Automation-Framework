package apitests;

import api.KoelApiSpec;
import io.restassured.response.Response;
import models.user.User;
import org.testng.annotations.Test;
import util.restUtils.RestUtil;

import static io.restassured.RestAssured.given;

public class UserTests extends KoelApiSpec {
    private final String URL = "https://qa.koel.app/api/me";
    @Test(description = "Get current user's profile info")
    public void getUserInfo() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .contentType("application/json")
                .accept("application/json")
                .when()
                .get(URL)
                .then().statusCode(200)
                .extract().response();
        User user = response.as(User.class);
        System.out.println(user.getId() + user.getEmail() + user.getName());
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
    }
}
