package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.AllSongsPage;
import pages.HomePage;
import pages.LoginPage;
import util.listeners.TestListener;


public class AllSongsTests extends BaseTest {
    LoginPage loginPage;
    AllSongsPage allSongsPage;
    HomePage homePage;
    public AllSongsTests() {
        super();
    }
    public void setupAllSongs() {
        homePage = new HomePage(getDriver());
        loginPage = new LoginPage(getDriver());
        allSongsPage = new AllSongsPage(getDriver());
        loginPage.loginValidCredentials();
    }

    @Test(description = "Play the first song on All Songs page")
    public void playFirstSong() {
        setupAllSongs();
        loginPage.allSongsPage();
               allSongsPage
                .checkHeaderTitle()
                .contextClickFirstSong()
                .choosePlayOption();
        Assert.assertTrue(allSongsPage.checkSongPlaying());
    }
    @Test(description = "Click on the Album Tab in the Info Panel")
    public void clickInfoPanelAlbumTab() {
        setupAllSongs();
        loginPage.allSongsPage();
        allSongsPage
                .checkHeaderTitle()
                .contextClickFirstSong()
                .choosePlayOption();
        homePage.clickAlbumTab();
        Assert.assertTrue(homePage.checkAlbumTabText());
    }
    @Test(description="Like all songs")
    public void likeAll(){
        setupAllSongs();
        loginPage.allSongsPage();
        allSongsPage.likeSongs();
        Assert.assertFalse(allSongsPage.checkUnliked());
    }
    @Test(description="Unlike all liked songs", dependsOnMethods = {"likeAll"})
    public void unlikeAll () {
        setupAllSongs();
        loginPage.allSongsPage();
        allSongsPage.unlikeSongs();
        Assert.assertTrue(allSongsPage.checkUnliked());
    }
    @Test(description = "Verify all song info is displayed properly")
    public void checkSongInfo() {
        setupAllSongs();
        loginPage.allSongsPage();
        Assert.assertTrue(allSongsPage.findSongInfo(), "Info is missing in one or more songs, check songs in All Songs page");
    }
    @Test(description = "Count the total number of playable songs and compare that to the total number of songs displayed in All Songs page header")
    public void totalPlayableSongsCount() {
        setupAllSongs();
        loginPage.allSongsPage();
        int manualCount = allSongsPage.getTotalSongsCount();
        int countDisplayedInHeader = Integer.parseInt(allSongsPage.getSongTotalFromHeader());
        TestListener.logInfoDetails("Total songs displayed in All Songs page header: " + countDisplayedInHeader);
        TestListener.logRsDetails("Manual count of songs listed in All Songs page: " + manualCount);
        TestListener.logAssertionDetails("Song total from header matches manual count: " + (countDisplayedInHeader == manualCount));
        Assert.assertNotEquals(manualCount, countDisplayedInHeader, "Double check the assertion, both counts now match");
    }
    @Test(description = "Verify song total is displayed in the All Songs page header")
    public void songTotalIsDisplayed() {
        setupAllSongs();
        loginPage.allSongsPage();
        Assert.assertTrue(allSongsPage.songTotalIsDisplayed(), "Song total not found");
    }
    @Test(description = "Verify total duration of songs is displayed in the All Songs page header")
    public void durationInHeader() {
        setupAllSongs();
        loginPage.allSongsPage();
        Assert.assertTrue(allSongsPage.totalDurationIsDisplayed(), "Total song duration not found");
    }
}
