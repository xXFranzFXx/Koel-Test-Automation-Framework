package api;

import models.playlist.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.restUtils.AssertionUtils;
import util.restUtils.RestUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.playlist.Playlist;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.ApiTestDataHandler;
import util.restUtils.AssertionUtils;
import util.restUtils.RestUtil;
import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApiTests extends KoelApiSpec{
    ApiTestDataHandler apiTestDataHandler = new ApiTestDataHandler();
    Map<String,Object> dataMap = new HashMap<>();
    Map<String, Object>  responseMap = new HashMap<>();
    JSONObject jsonObject;
    String responseBody;
    @BeforeClass
    public void getEnv() {
        setEnv();
        getAuthRequestSpec();
    }
    @Test
    public void createPlaylist() {
        jsonObject = new JSONObject();
        jsonObject.put("name", "newPlaylist");
        String payload = jsonObject.toString();
        String URL = "https://qa.koel.app/api/playlist";

        Response response = given()
                .spec(getAuthRequestSpec())
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                .post(URL)
                .then().statusCode(200)
                .extract().response();

        Playlist playlist = response.as(Playlist.class);
        dataMap.put("name", playlist.getName());
        dataMap.put("id", playlist.getId());
        apiTestDataHandler.setApiTestDataInMap(dataMap);
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec(), payload);
        AssertionUtils.assertExpectedValuesWithJsonPath(response, dataMap);
    }
    @Test
    public void verifyPlaylistName() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .get("https://qa.koel.app/api/playlist")
                .then().statusCode(200)
                .extract().response();
        Playlist[] playlists = response.as(Playlist[].class);
        System.out.println(Arrays.toString(playlists));
        responseMap.put("id", playlists[0].getId());
        responseMap.put("name", playlists[0].getName());
        Assert.assertEquals(playlists[0].getName(), "newPlaylist");
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
    }
}


