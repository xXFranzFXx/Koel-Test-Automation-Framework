package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

import java.net.MalformedURLException;


/**
 * Story:
 * As a user, I want to be able to observe Lyrics, Artist and Album information in the Info panel
 * Acceptance Criteria:
 * 1 Album name and cover should be displayed for a playing song
 * 2 Lyrics should be displayed for a playing song
 * 3 Artist name should be displayed for a playing song
 * 4 Info panel should appear after clicking on 'INFO' button on the music player
 * 5 Info panel should hide after clicking on 'INFO'  button on the music player
 * 6 User should be able to shuffle songs on 'INFO' panel
 */
public class InfoPanelTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    private final String searchArtist = "Grav";

    private void checkInfoPanel() {
        if(!homePage.checkVisibility()){
            homePage.clickInfoButton();
        }
    }
    public InfoPanelTests() {
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
        homePage = loginPage.loginValidCredentials();
    }
    @AfterMethod
    public void close() {
        closeBrowser();
    }

    @Test(description = "checks for visibility of the info panel upon logging in")
    public void toggleInfoPanel() {
        Reporter.log("Info panel is visible " + homePage.checkVisibility(), true);
        if(homePage.checkVisibility()) {
            homePage.clickInfoBtnActive();
            Assert.assertFalse(homePage.checkVisibility());
            Reporter.log("Clicked Info Active button, now it's visible? " + homePage.checkVisibility(), true);
            homePage.clickInfoButton();
            Assert.assertTrue(homePage.checkVisibility());
            Reporter.log("Info panel is visible! ", true);
        } else {
            homePage.clickInfoButton();
            Assert.assertTrue(homePage.checkVisibility());
            homePage.clickInfoBtnActive();
            Assert.assertFalse(homePage.checkVisibility());
            Reporter.log("Info Panel is invisible", true);

        }

    }
    @Test(description = "Login, search for an artist and play song, then test shuffle play button in the Album Tab")
    public void checkShufflePlayBtn() {
        checkInfoPanel();
        homePage.searchSong(searchArtist)
                .clickSearchResultThumbnail()
                .clickAlbumTab();
        homePage.clickAlbumTabShuffleBtn();
        Assert.assertTrue(homePage.checkQueueTitle());
        Reporter.log("Successfully shuffled songs using Album tab shuffle button", true);

    }
    @Test(description = "click each tab and verify correct info is displayed in info panel then press the shuffle button")
    public void checkInfoPanelTabs() {
        String lyricsInfoText = "No lyrics available. Are you listening to Bach?";
        String albumInfoText = "Play all songs in the album Dark Days EP";
        checkInfoPanel();
        try {
            homePage.searchSong(searchArtist)
                    .clickSearchResultThumbnail();
            String displayedLyricsString;
            displayedLyricsString = homePage.clickLyricsTab();
            Assert.assertEquals(displayedLyricsString, lyricsInfoText);
            Reporter.log("Verified functionality of Lyrics tab in info panel", true);

        }catch(Exception e) {
            Reporter.log("Failed to verify Lyrics tab" + e);
            Assert.assertFalse(false);
        }
        try {
            String displayedArtistString;
            displayedArtistString = homePage.clickArtistTab();
            Assert.assertEquals(displayedArtistString, searchArtist);
            Reporter.log("Verified functionality of Artist tab in info panel", true);

        }catch(Exception e) {
            Reporter.log("Failed to verify Artist tab" + e);
            Assert.assertFalse(false);

        }
        try {
        String displayedAlbumString;
        displayedAlbumString = homePage.clickAlbumTab();
        Assert.assertEquals(displayedAlbumString, albumInfoText);
            Reporter.log("Verified functionality of Album tab in info panel", true);

        }catch(Exception e) {
            Reporter.log("Failed to verify Album tab" + e);
            Assert.assertFalse(false);

        }
        Reporter.log("Finished testing functionality of all tabs in info panel", true);
    }
}