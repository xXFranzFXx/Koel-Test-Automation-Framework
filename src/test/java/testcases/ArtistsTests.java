package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.ArtistsPage;
import pages.LoginPage;
import pages.SearchPage;
import util.listeners.TestListener;

public class ArtistsTests extends BaseTest {
    LoginPage loginPage;
    SearchPage searchPage;
    ArtistsPage artistsPage;
    public ArtistsTests() {
        super();
    }
    public void setupArtists() {
        loginPage = new LoginPage(getDriver());
        searchPage = new SearchPage(getDriver());
        loginPage.loginValidCredentials();
        artistsPage = new ArtistsPage(getDriver());
        artistsPage.navigateToArtistsPage();
    }

    @Test(description = "Play all songs by an artist")
    public void playAllSongsByArtist () {
        setupArtists();
       artistsPage
                .rightClickAlbum()
                .selectPlayAll();
        Assert.assertTrue(artistsPage.soundbarIsDisplayed());
        Reporter.log("Added all songs from selected artist album to current queue");
    }
    @Test(description = "Verify artists are displayed")
    public void artistsAreDisplayed() {
        setupArtists();
        TestListener.logAssertionDetails("All artists are present: " + artistsPage.artistsArePresent());
        Assert.assertTrue(artistsPage.artistsArePresent(), "Artists are not displayed");
    }
    @Test(description = "Verify artists is displayed in search results")
    public void searchArtist() {
        setupArtists();
        String search = "dark";
        artistsPage.searchArtist(search);
        TestListener.logAssertionDetails("Artist shows up from search: " + searchPage.artistExists());
        Assert.assertTrue(searchPage.artistExists());
    }
    @Test(description = "Verify user can play songs from selected artist")
    public void playArtistSongs() {
        setupArtists();
        artistsPage.clickArtistPlay();
        TestListener.logAssertionDetails("User can play song from artist: " + artistsPage.songIsPlaying());
        Assert.assertTrue(artistsPage.songIsPlaying());
    }
}
