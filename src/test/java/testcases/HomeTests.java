package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.AllSongsPage;
import pages.HomePage;
import pages.LoginPage;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class HomeTests extends BaseTest {
    LoginPage loginPage;
    AllSongsPage allSongsPage;
    HomePage homePage;
    Map<String, String> dataMap = new HashMap<>();
    public HomeTests() {
        super();
    }
    public void setupHome(){
        loginPage = new LoginPage(getDriver());
        loginPage.loginValidCredentials();
    }
    @Test(description = "User can create a playlist", priority = 1)
    public void createPlaylist() {
        setupHome();
        String playlist = generatePlaylistName(5);
        dataMap.put("playlist", playlist);
        homePage = new HomePage(getDriver());
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName(playlist);
        Assert.assertTrue(homePage.playlistAddedToMenu(playlist));
    }
    @Test(description = "Add a song to a playlist", priority = 2, dependsOnMethods = {"createPlaylist"})
    public void addSongToPlaylist() {
        setupHome();
        homePage = new HomePage(getDriver());
        homePage.searchSong("dark")
                .clickViewAllButton()
                .clickFirstSearchResult()
                .clickGreenAddToBtn()
                .selectPlaylistToAddTo(dataMap.get("playlist"));
        Assert.assertTrue(homePage.notificationMsg());
        Reporter.log("Added song to playlist", true);
    }
    @Test(description = "hover cursor over the play button")
    public void hoverOverPlayBtn() throws InterruptedException {
        setupHome();
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.hoverPlay());
    }

    @Test(description = "verify songs exist in recenlty played section")
    public void recentlyPlayedExists() {
        setupHome();
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.recentlyPlayedListExists());
        Reporter.log("Recently Played song list size is:" + homePage.recentlyPlayedListSize(), true);
    }
    @Test(description = "verify songs exist in recently added section")
    public void checkRecentlyAdded() {
        setupHome();
        homePage = new HomePage(getDriver());
//        homePage.clickRPViewAllBtn();
        Assert.assertTrue(homePage.recentlyAddedListHasAlbumTitles());
    }
    @Test(description = "check that recently added songs have title and also have a shuffle and download icon")
    public void checkRASongsOnHover() {
        setupHome();
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.checkRAListButtonsOnHover());

    }
    @Test(description = "verify functionality of 'about' link")
    public void clickAboutLink() {
        setupHome();
        homePage = new HomePage(getDriver());
        homePage.clickAboutLink();
        Assert.assertTrue(homePage.aboutModalVisible());
    }
    @Test(description =  "verify the 'about' modal closes")
    public void modalCloses() {
        setupHome();
        homePage = new HomePage(getDriver());
        homePage.clickAboutLink()
                .closeModalAndLogOut();

    }
  @Test(description = "delete playlist created", dependsOnMethods = { "addSongToPlaylist" })
    public void deletePlaylist() {
        setupHome();
        String playlistName = dataMap.get("playlist");
        homePage = new HomePage(getDriver());
        homePage.deleteRegularPlaylistWithSong(playlistName);
        dataMap.clear();
        Assert.assertTrue(homePage.notificationMsg());
    }

}
