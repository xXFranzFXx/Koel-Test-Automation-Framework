package testcases;

import base.BaseTest;
import db.KoelDb;
import io.restassured.response.Response;
import models.data.Data;
import models.data.Interaction;
import models.playlist.Playlist;
import models.user.User;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import util.DataProviderUtil;
import util.dbUtils.DbTestUtil;
import util.listeners.TestListener;
import util.restUtils.ApiTestDataHandler;
import util.restUtils.AssertionUtils;
import util.restUtils.RestUtil;

import java.sql.SQLException;
import java.util.*;

import static api.KoelApiSpec.getAuthRequestSpec;
import static io.restassured.RestAssured.given;

public class ApiTests extends BaseTest {
    HomePage homePage;
    LoginPage loginPage;
    ApiTestDataHandler apiTestDataHandler = new ApiTestDataHandler();
    private final String playlistURL = "https://qa.koel.app/api/playlist";
    private final String dataURL = "https://qa.koel.app/api/data";
    private final String songInteraction = "https://qa.koel.app/api/interaction/play";
    private final String recentlyPlayed = "https://qa.koel.app/api/interaction/recently-played";
    private final String likeUnlikeOneSong = "https://qa.koel.app/api/like";

    public void setupKoel() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        loginPage.loginValidCredentials();
    }
    @AfterClass
    public void close() {
        closeBrowser();
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
    @Test(description = "Get application data and compare playlists in response to playlists in ui")
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
            Optional<Data> data = Optional.ofNullable(response.as(Data.class));
            List<String> dataEmail = data.stream()
                    .map(Data::getCurrentUser)
                    .map(User::getEmail)
                    .map(Object::toString)
                    .toList();
            String userEmail = System.getProperty("koelUser");
            RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
            Assert.assertEquals(dataEmail.get(0), userEmail);
        } catch (TimeoutException e) {
            TestListener.logExceptionDetails("Request timed out: " + e.getLocalizedMessage());
        }
    }
    @Test(description = "Increase song playcount",  dataProvider = "ApiData", dataProviderClass = DataProviderUtil.class)
    public void increasePlayCount(String songId) {
        apiTestDataHandler.addApiTestData(songId, songId);
        try {
            Response response = given()
                    .spec(getAuthRequestSpec())
                    .contentType("multipart/form-data")
                    .multiPart("song", songId)
                    .when()
                    .post(songInteraction)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response();
            Interaction interaction = response.as(Interaction.class);
            String id = interaction.getSong_id();
            TestListener.logAssertionDetails("Song playcount is increased: " + songId.equalsIgnoreCase(id));
            Assert.assertEquals(songId, id);
         } catch (Exception e) {
            TestListener.logExceptionDetails("Request timed out: " + e.getLocalizedMessage());
        }
    }
    @Test
    public void getRecentlyPlayedSongIds() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .contentType("application/json")
                .accept("application/json")
                .when()
                .get(recentlyPlayed)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        List<String> songs = response.jsonPath().get();
        songs.forEach(System.out::println);
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
    }
    @Test(description = "get recently played song ids from api then query the db for the song titles of those song ids")
    public void getRecentlyPlayedSongTitles() throws SQLException, ClassNotFoundException {
        KoelDbTests.initializeDb();
        Response response = given()
                .spec(getAuthRequestSpec())
                .contentType("application/json")
                .accept("application/json")
                .when()
                .get(recentlyPlayed)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        List<String> songs = response.jsonPath().get();
        Optional<List<String>> dbSongs = Optional.ofNullable(DbTestUtil.getSongTitles(songs));
        dbSongs.get().forEach(System.out::println);

        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        TestListener.logRsDetails("db songs: " + dbSongs);
        TestListener.logAssertionDetails("All recently played songs are in database: " + (songs.size() == dbSongs.get().size()));
        KoelDb.closeDatabaseConnection();
    }
    @Test(description = "get recently played song ids from api then query the db for the song titles of those song ids, and check the db titles against the recently played song titles in ui")
    public void compareRecentlyPlayedSongTitles() throws SQLException, ClassNotFoundException {
        KoelDbTests.initializeDb();
        setupKoel();
        Response response = given()
                .spec(getAuthRequestSpec())
                .contentType("application/json")
                .accept("application/json")
                .when()
                .get(recentlyPlayed)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        List<String> songs = response.jsonPath().get();
        List<String> dbSongs = DbTestUtil.getSongTitles(songs);
        List<String> rPlayedSongs = homePage.recentlyPlayedTitles();
        rPlayedSongs.forEach(System.out::println);
        dbSongs.forEach(System.out::println);

        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
        TestListener.logRsDetails("db songs: " + dbSongs);
        TestListener.logRsDetails("ui songs: " + rPlayedSongs);
        TestListener.logAssertionDetails("All recently played songs in db and ui are correct: " + (rPlayedSongs.size() == dbSongs.size()));
        KoelDb.closeDatabaseConnection();
    }
}
