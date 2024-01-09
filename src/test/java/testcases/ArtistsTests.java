package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.ArtistsPage;
import pages.LoginPage;

import java.net.MalformedURLException;

public class ArtistsTests extends BaseTest {
    LoginPage loginPage;
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
    }
    @AfterMethod
    public void close() {
        closeBrowser();
    }

    @Test(description = "Play all songs by an artist")
    public void playAllSongsByArtist () {
       artistsPage
                .rightClickAlbum()
                .selectPlayAll();
        Assert.assertTrue(artistsPage.soundbarIsDisplayed());
        Reporter.log("Added all songs from selected artist album to current queue");
    }
}
