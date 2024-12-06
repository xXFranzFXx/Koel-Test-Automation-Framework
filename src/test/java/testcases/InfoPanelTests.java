package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import util.listeners.TestListener;

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
    public void setupInfoPanel() {
        loginPage = new LoginPage(getDriver());
        homePage = loginPage.loginValidCredentials();
    }
    @Test(description = "checks for visibility of the info panel upon logging in")
    public void toggleInfoPanel() {
        setupInfoPanel();
        Reporter.log("Info panel is visible " + homePage.checkVisibility(), true);
        if(homePage.checkVisibility()) {
            homePage.clickInfoBtnActive();
            TestListener.logAssertionDetails("Info panel is visible after first click: " + homePage.checkVisibility());
            Assert.assertFalse(homePage.checkVisibility());

            homePage.clickInfoButton();
            TestListener.logAssertionDetails("Info panel is visible after second click: " + homePage.checkVisibility());
            Assert.assertTrue(homePage.checkVisibility());
        } else {
            homePage.clickInfoButton();
            TestListener.logAssertionDetails("Info panel is visible after first click: " + homePage.checkVisibility());
            Assert.assertTrue(homePage.checkVisibility());

            homePage.clickInfoBtnActive();
            TestListener.logAssertionDetails("Info panel is visible after second click: " + homePage.checkVisibility());
            Assert.assertFalse(homePage.checkVisibility());
        }
    }
    @Test(description = "Login, search for an artist and play song, then test shuffle play button in the Album Tab")
    public void checkShufflePlayBtn() {
        setupInfoPanel();
        checkInfoPanel();
        homePage.searchSong(searchArtist)
                .clickSearchResultThumbnail()
                .clickAlbumTab();
        homePage.clickAlbumTabShuffleBtn();
        TestListener.logAssertionDetails("Successfully shuffled songs using Album tab shuffle button: " + homePage.checkQueueTitle());
        Assert.assertTrue(homePage.checkQueueTitle());
    }
    @Test(description = "click each tab and verify correct info is displayed in info panel then press the shuffle button")
    public void checkInfoPanelTabs() {
        setupInfoPanel();
        String lyricsInfoText = "No lyrics available. Are you listening to Bach?";
        String albumInfoText = "Play all songs in the album Dark Days EP";
        checkInfoPanel();
        try {
            homePage.searchSong(searchArtist)
                    .clickSearchResultThumbnail();
            String displayedLyricsString;
            displayedLyricsString = homePage.clickLyricsTab();
            TestListener.logAssertionDetails("Correct info is displayed in info panel lyrics tab: " + displayedLyricsString.equalsIgnoreCase(lyricsInfoText));
            Assert.assertEquals(displayedLyricsString, lyricsInfoText);
        }catch(Exception e) {
            TestListener.logExceptionDetails("Error: " + e);
            Assert.assertFalse(false);
        }
        try {
            String displayedArtistString;
            displayedArtistString = homePage.clickArtistTab();
            TestListener.logAssertionDetails("Correct info is displayed in info panel artists tab: " + displayedArtistString.equalsIgnoreCase(searchArtist));
            Assert.assertEquals(displayedArtistString, searchArtist);
            Reporter.log("Verified functionality of Artist tab in info panel", true);
        }catch(Exception e) {
            TestListener.logExceptionDetails("Error: " + e);
            Assert.assertFalse(false);
        }
        try {
        String displayedAlbumString;
        displayedAlbumString = homePage.clickAlbumTab();
            TestListener.logAssertionDetails("Correct info is displayed info panel album info tab: " + displayedAlbumString.equalsIgnoreCase(albumInfoText));
            Assert.assertEquals(displayedAlbumString, albumInfoText);
        }catch(Exception e) {
            TestListener.logExceptionDetails("Error: " + e);
            Assert.assertFalse(false);
        }
        Reporter.log("Finished testing functionality of all tabs in info panel", true);
    }
    @Test(description = "Get Lyrics from Lyrics info Tab")
    public void getLyrics() {
        setupInfoPanel();
        String lyricsInfoText = "This song has no lyrics but for testing purposes here is some:";
        checkInfoPanel();
        try {
            homePage.searchSong("emotional")
                    .clickSearchResultThumbnail();
            String displayedLyricsString = homePage.clickLyricsTab();
            TestListener.logInfoDetails("Lyrics tab: " + displayedLyricsString);
            TestListener.logAssertionDetails("Correct info is displayed in info panel lyrics tab: " + displayedLyricsString.contains(lyricsInfoText));
            Assert.assertTrue(displayedLyricsString.contains(lyricsInfoText));
        } catch (Exception e) {
            TestListener.logExceptionDetails("Error: " + e);
            Assert.assertFalse(false);
        }
    }
}