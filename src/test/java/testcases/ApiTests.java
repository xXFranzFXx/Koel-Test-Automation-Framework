package testcases;

import base.BaseTest;
import io.restassured.response.Response;
import models.playlist.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import util.listeners.TestListener;
import util.restUtils.ApiTestDataHandler;
import util.restUtils.AssertionUtils;
import util.restUtils.RestUtil;

import java.util.*;
import java.util.stream.Collectors;

import static api.KoelApiSpec.getAuthRequestSpec;
import static io.restassured.RestAssured.given;

public class ApiTests extends BaseTest {
    HomePage homePage;
    LoginPage loginPage;
    ApiTestDataHandler apiTestDataHandler = new ApiTestDataHandler();
    Map<String,List<String>> playlistMap = new HashMap<>();
    Map<String, Object>  dataMap = new HashMap<>();
    private final String URL = "https://qa.koel.app/api/playlist";
    public void setupKoel() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        loginPage.loginValidCredentials();
    }
    @Test(description = "Create a playlist through api, and verify new playlist appears in UI")
    public void createPlaylist() {
        String payload = apiTestDataHandler.createPayload("name", "newPlaylist");
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
       try {
           setupKoel();
           TestListener.logAssertionDetails("Playlist " + playlist.getName() + " appears in UI: " + homePage.playlistAddedToMenu(playlist.getName()));
           Assert.assertTrue(homePage.playlistAddedToMenu(playlist.getName()));
       } catch (Exception e) {
           TestListener.logExceptionDetails("Could not verify playlist created: " + e.getLocalizedMessage());
       }
    }

    @Test
    public void verifyPlaylistNames() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .get(URL)
                .then().statusCode(200)
                .extract().response();
        List<String> names = RestUtil.getPlaylistNames(response);
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        try {
            setupKoel();
            List<String> playlistNames = homePage.getPlaylistNames();
            TestListener.logInfoDetails("Playlists from api call: " + names);
            TestListener.logInfoDetails("Playlists from UI: " + playlistNames);
            TestListener.logAssertionDetails("Data from api matches UI: " + names.equals(playlistNames));
            Assert.assertEqualsNoOrder(names, playlistNames);
        } catch (Exception e) {
            TestListener.logExceptionDetails("Could not verify user playlists: " + e.getLocalizedMessage());
        }
    }
    @Test(description="Delete a playlist using api call, then verify playlist no longer appears in UI", dependsOnMethods = {"verifyPlaylistNames"})
    public void deletePlaylist() {
        String playlst = dataMap.get("id").toString();
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .delete(URL+"/"+playlst)
                .then().statusCode(200)
                .extract().response();
        int responseCode = response.getStatusCode();
        Assert.assertEquals(responseCode, 200);
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        try {
            setupKoel();
            dataMap.put("userPlaylists", homePage.getPlaylistNames());
            TestListener.logInfoDetails("Playlist deleted through api: " + dataMap.get("name"));
            TestListener.logAssertionDetails("Playlist no longer appears in UI: " + !homePage.playlistAddedToMenu(dataMap.get("name").toString()));
            Assert.assertFalse(homePage.playlistAddedToMenu(dataMap.get("name").toString()));
        } catch (Exception e){
            TestListener.logExceptionDetails("Error deleting playlist, or cannot verify playlist deleted in UI: " + e.getLocalizedMessage());

        }
    }
    @Test(description = "After deleting playlist in api, verify user playlists in UI match user playlists from api call", dependsOnMethods ={"deletePlaylist"} )
    public void getUserPlaylistNames() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .get(URL)
                .then().statusCode(200)
                .extract().response();
        int responseCode = response.getStatusCode();
        Assert.assertEquals(responseCode, 200);
        Optional<Object> lists = Optional.ofNullable(dataMap.get("userPlaylists"));
        List<String> apiPlaylists = RestUtil.getPlaylistNames(response);
        List<String> uiPlaylists = lists.stream().map(Object::toString).toList();
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        TestListener.logAssertionDetails("Playlists in UI match api: " + uiPlaylists.equals(apiPlaylists));
        Assert.assertEquals(uiPlaylists, apiPlaylists);
    }
}
