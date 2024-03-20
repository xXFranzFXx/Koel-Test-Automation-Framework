package apitests;

import api.KoelApiSpec;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.DataProviderUtil;
import util.restUtils.AssertionUtils;
import util.restUtils.RestUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class LoginTests extends  KoelApiSpec{
    private final String URL = "https://qa.koel.app/api/me";
    Map<String,Object> dataMap = new HashMap<>();
    @Test(description="Verify response code 302 when logging in with missing login credentials", dataProvider="LoginData", dataProviderClass = DataProviderUtil.class)
    public void loginWithWrongCredentials(String email, String password) {
        Response response = given()
                .spec(getLoginSpec(email, password))
                .post()
                .then()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), 302);
        RestUtil.getRequestDetailsForLog(response, getLoginSpec(email, password));

    }
    @Test(description="Verify response code 401 when logging in with incorrectly formatted email and password", dataProvider="LoginData", dataProviderClass = DataProviderUtil.class)
    public void loginWithWrongFormat(String email, String password) {
        Response response = given()
                .spec(getLoginSpec(email, password))
                .post()
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 401);
        RestUtil.getRequestDetailsForLog(response, getLoginSpec(email, password));

    }
    @Test(description="Verify token is received when login with valid credentials", dataProvider="LoginData", dataProviderClass = DataProviderUtil.class)
    public void loginWithValidCredentials(String email, String password) {
        Response response = given()
                .spec(getLoginSpec(email, password))
                .post()
                .then()
                .statusCode(200)
                .extract().response();
        String token = response.path("token");
        Object responseBody = response.jsonPath().get();
        dataMap.put("token", token);
        Assert.assertTrue(responseBody.toString().contains(token));
        AssertionUtils.assertExpectedValuesWithJsonPath(response, dataMap);
        RestUtil.getRequestDetailsForLog(response, getLoginSpec(email, password));
    }
    @Test(description="Verify user can Log out")
    public void logUserOut() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .delete(URL)
                .then()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), 204);
    }
}
