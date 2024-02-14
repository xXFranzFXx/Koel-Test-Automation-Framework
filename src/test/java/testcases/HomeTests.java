package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.AllSongsPage;
import pages.HomePage;
import pages.LoginPage;

import java.net.MalformedURLException;

public class HomeTests extends BaseTest {
    LoginPage loginPage;
    AllSongsPage allSongsPage;
    HomePage homePage;
    public HomeTests() {
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
        loginPage.loginValidCredentials();
    }

    @Test(description = "User can create a playlist", priority = 1)
    public void createPlaylist() {
        homePage = new HomePage(getDriver());
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName("playlist");
        Assert.assertTrue(homePage.playlistAddedToMenu("playlist"));
    }
    @Test(description = "Add a song to a playlist", priority = 2, dependsOnMethods = {"createPlaylist"})
    public void addSongToPlaylist() {
        homePage = new HomePage(getDriver());
        homePage.searchSong("dark")
                .clickViewAllButton()
                .clickFirstSearchResult()
                .clickGreenAddToBtn()
                .selectPlaylistToAddTo();
        Assert.assertTrue(homePage.notificationMsg());
        Reporter.log("Added song to playlist", true);
    }
    @Test(description = "hover cursor over the play button")
    public void hoverOverPlayBtn() throws InterruptedException {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.hoverPlay());
    }

    @Test(description = "verify songs exist in recenlty played section")
    public void recentlyPlayedExists() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.recentlyPlayedListExists());
        Reporter.log("Recently Played song list size is:" + homePage.recentlyPlayedListSize(), true);
    }
    @Test(description = "verify songs exist in recently added section")
    public void checkRecentlyAdded() {
        homePage = new HomePage(getDriver());
//        homePage.clickRPViewAllBtn();
        Assert.assertTrue(homePage.recentlyAddedListHasAlbumTitles());
    }
    @Test(description = "check that recently added songs have title and also have a shuffle and download icon")
    public void checkRASongsOnHover() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.checkRAListButtonsOnHover());

    }
    @Test(description = "verify functionality of 'about' link")
    public void clickAboutLink() {
        homePage = new HomePage(getDriver());
        homePage.clickAboutLink();
        Assert.assertTrue(homePage.aboutModalVisible());
    }
    @Test(description =  "verify the 'about' modal closes")
    public void modalCloses() {
        homePage = new HomePage(getDriver());
        homePage.clickAboutLink()
                .closeModalAndLogOut();

    }
//  @Test(description = "delete playlist created", dependsOnMethods = { "createPlaylist" })
    public void deletePlaylist() {
        homePage = new HomePage(getDriver());
        homePage.contextClickFirstPlDelete();
        Assert.assertTrue(homePage.notificationMsg());
    }
    @Test(description = "delete all playlists", priority=3)
    public void deleteAllPlaylists() {
        homePage = new HomePage(getDriver());
        homePage.deleteAllPlaylists();
        Assert.assertTrue(homePage.playlistsEmpty());
    }
}
