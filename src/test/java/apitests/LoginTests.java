package apitests;

import api.KoelApiSpec;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.restUtils.AssertionUtils;
import util.restUtils.RestUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class LoginTests extends  KoelApiSpec{
    Map<String,Object> dataMap = new HashMap<>();
    @Test
    public void loginValidCredentials() {
        Response response = given()
                .spec(getLoginSpec())
                .post()
                .then()
                .statusCode(200)
                .extract().response();
        String token = response.path("token");
        Object responseBody = response.jsonPath().get();
        dataMap.put("token", token);
        Assert.assertTrue(responseBody.toString().contains(token));
        AssertionUtils.assertExpectedValuesWithJsonPath(response, dataMap);
        RestUtil.getRequestDetailsForLog(response, getLoginSpec());
    }
}
