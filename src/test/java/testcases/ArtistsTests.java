package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.ArtistsPage;
import pages.LoginPage;
import pages.SearchPage;
import util.listeners.TestListener;

import java.net.MalformedURLException;

public class ArtistsTests extends BaseTest {
    LoginPage loginPage;
    SearchPage searchPage;
    ArtistsPage artistsPage;
    public ArtistsTests() {
        super();
    }
    @BeforeClass
    public void setEnv() {
        loadEnv();
    }
    @BeforeMethod
    @Parameters({"baseURL"})
    public void setup(String baseURL) throws MalformedURLException {
        setupBrowser(baseURL);
        loginPage = new LoginPage(getDriver());
        artistsPage = loginPage.loginValidCredentials().clickArtists();
        searchPage = new SearchPage(getDriver());
    }


    @Test(description = "Play all songs by an artist")
    public void playAllSongsByArtist () {
       artistsPage
                .rightClickAlbum()
                .selectPlayAll();
        Assert.assertTrue(artistsPage.soundbarIsDisplayed());
        Reporter.log("Added all songs from selected artist album to current queue");
    }
    @Test(description = "Verify artists are displayed")
    public void artistsAreDisplayed() {
        TestListener.logAssertionDetails("All artists are present: " + artistsPage.artistsArePresent());
        Assert.assertTrue(artistsPage.artistsArePresent(), "Artists are not displayed");
    }
    @Test(description = "Verify artists is displayed in search results")
    public void searchArtist() {
        String search = "dark";
        artistsPage.searchArtist(search);
        TestListener.logAssertionDetails("Artist shows up from search: " + searchPage.artistExists());
        Assert.assertTrue(searchPage.artistExists());
    }
    @Test(description = "Verify user can play songs from selected artist")
    public void playArtistSongs() {
        artistsPage.clickArtistPlay();
        TestListener.logAssertionDetails("User can play song from artist: " + artistsPage.songIsPlaying());
        Assert.assertTrue(artistsPage.songIsPlaying());
    }
}
