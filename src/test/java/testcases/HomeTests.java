package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.HomePage;
import pages.LoginPage;
import util.listeners.TestListener;


import java.util.HashMap;
import java.util.Map;

public class HomeTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    Map<String, String> dataMap = new HashMap<>();
    public HomeTests() {
        super();
    }
    public void setupHome(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        loginPage.loginValidCredentials();
    }
    @Test(description = "Volume slider control appears when mouse hovers over volume control")
    public void checkForVolumeSlider() {
        setupHome();
        homePage.checkVolumeSlider();
        TestListener.logAssertionDetails("Volume slider appears when hovering over volume button: " + homePage.volumeSliderDisplayed());
        Assert.assertTrue(homePage.volumeSliderDisplayed());
    }
    @Test(description = "User can create a playlist", priority = 1)
    public void createPlaylist() {
        setupHome();
        String playlist = generatePlaylistName(5);
        dataMap.put("playlist", playlist);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName(playlist);
        TestListener.logAssertionDetails("User can create a playlist: " + homePage.playlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.playlistAddedToMenu(playlist));
    }
    @Test(description = "Add a song to a playlist", priority = 2, dependsOnMethods = {"createPlaylist"})
    public void addSongToPlaylist() {
        setupHome();
        homePage.searchSong("dark")
                .wait(2)
                .clickSearchViewAllBtn()
                .clickFirstSearchResult()
                .clickGreenAddToBtn()
                .wait(2)
                .selectPlaylistToAddTo(dataMap.get("playlist"));
        TestListener.logAssertionDetails("Added song to playlist: " + homePage.notificationMsg());
        Assert.assertTrue(homePage.notificationMsg());
    }
    @Test(description = "hover cursor over the play button")
    public void hoverOverPlayBtn(){
        setupHome();
        Assert.assertTrue(homePage.hoverPlay());
    }

    @Test(description = "verify songs exist in recenlty played section")
    public void recentlyPlayedExists() {
        setupHome();
        TestListener.logInfoDetails("Recently Played song list size is: " + homePage.recentlyPlayedListSize());
        Assert.assertTrue(homePage.recentlyPlayedListExists());
    }
    @Test(description = "verify songs exist in recently added section")
    public void checkRecentlyAdded() {
        setupHome();
        TestListener.logAssertionDetails("Recently Added songs are displayed: " + homePage.recentlyAddedListHasAlbumTitles());
        Assert.assertTrue(homePage.recentlyAddedListHasAlbumTitles());
    }
    @Test(description = "check that recently added songs have title and also have a shuffle and download icon")
    public void checkRASongsOnHover() {
        setupHome();
        boolean checkRASongs = homePage.checkRAListButtonsOnHover();
        TestListener.logAssertionDetails("Recently Added songs have title, shuffle icon and download icon: " + checkRASongs);
        Assert.assertTrue(checkRASongs);
    }
    @Test(description = "verify visibility of 'about' link")
    public void clickAboutLink() {
        setupHome();
        homePage.clickAboutLink();
        TestListener.logAssertionDetails("'About' link visible: " + homePage.aboutModalVisible());
        Assert.assertTrue(homePage.aboutModalVisible());
    }
    @Test(description =  "verify the 'about' modal closes")
    public void modalCloses() {
        setupHome();
        homePage.clickAboutLink()
                .closeModalAndLogOut();
        TestListener.logAssertionDetails("'About' modal closes: " + loginPage.getRegistrationLink());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }
   @Test(description="verify recently added list has liked button")
   public void checkRaLikedButtons() {
        setupHome();
        TestListener.logAssertionDetails("Recently Added songs have liked buttons: " + homePage.recentlyAddedHasLikedButtons());
        Assert.assertTrue(homePage.recentlyAddedHasLikedButtons());
   }
   @Test(description = "delete playlist created", dependsOnMethods = { "addSongToPlaylist" })
    public void deletePlaylist() {
        setupHome();
        String playlistName = dataMap.get("playlist");
        homePage.deleteRegularPlaylistWithSong(playlistName);
        dataMap.clear();
        TestListener.logAssertionDetails("Playlist "+playlistName+" deleted: " + homePage.notificationMsg());
        Assert.assertTrue(homePage.notificationMsg());
    }
}
