package testcases;

import base.BaseTest;
import io.restassured.response.Response;
import models.album.Album;
import models.data.Data;
import models.playlist.Playlist;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
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
    private final String playlistURL = "https://qa.koel.app/api/playlist";
    private final String dataURL = "https://qa.koel.app/api/data";

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
                .post(playlistURL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();
        Playlist playlist = response.as(Playlist.class);
        apiTestDataHandler.addApiTestData("name", playlist.getName());
        apiTestDataHandler.addApiTestData("id", playlist.getId());
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec(), payload);
        AssertionUtils.assertExpectedValuesWithJsonPath(response, apiTestDataHandler.getApiTestDataInMap());
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
                .get(playlistURL)
                .then()
                .assertThat()
                .statusCode(200)
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
        String playlst = apiTestDataHandler.getApiTestValue("id");
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .delete(playlistURL+"/"+playlst)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        try {
            setupKoel();
            apiTestDataHandler.addApiTestData("userPlaylists", homePage.getPlaylistNames());
            String name = apiTestDataHandler.getApiTestValue("name");
            TestListener.logInfoDetails("Playlist deleted through api: " + name);
            TestListener.logAssertionDetails("Playlist no longer appears in UI: " + !homePage.playlistAddedToMenu(name));
            Assert.assertFalse(homePage.playlistAddedToMenu(name));
        } catch (Exception e){
            TestListener.logExceptionDetails("Error deleting playlist, or cannot verify playlist deleted in UI: " + e.getLocalizedMessage());
        }
    }
    @Test(description = "After deleting playlist in api, verify user playlists in UI match user playlists from api call", dependsOnMethods ={"deletePlaylist"} )
    public void getUserPlaylistNames() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .when()
                .get(playlistURL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        Optional<Object> lists = Optional.ofNullable(apiTestDataHandler.getApiTestValue("userPlaylists"));
        List<String> apiPlaylists = RestUtil.getPlaylistNames(response);
        List<String> uiPlaylists = lists.stream().map(Object::toString).toList();

        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        TestListener.logAssertionDetails("Playlists in UI match api: " + uiPlaylists.equals(apiPlaylists));
        Assert.assertEquals(uiPlaylists, apiPlaylists);
    }
    @Test(enabled = false, description = "Get application data and compare playlists in response to playlists in ui")
    public void getApplicationData() {
        try {
            Response response = given()
                    .spec(getAuthRequestSpec())
                    .when()
                    .get(dataURL)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response();
            Data data = response.as(Data.class);
            List<String> playlistNames = Arrays.stream(data.getPlaylists())
                    .map(Playlist::getName)
                    .toList();
            boolean song = playlistNames.contains(System.getProperty("checkSong"));
            RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
            TestListener.logAssertionDetails("Response contains" + System.getProperty("checkSong") + ": " + song);
            Assert.assertTrue(song);
        } catch (TimeoutException e) {
            TestListener.logExceptionDetails("Request timed out: " + e.getLocalizedMessage());
        }
    }
}
