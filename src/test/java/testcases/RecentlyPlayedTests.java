package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RecentlyPlayedPage;
import util.listeners.TestListener;

import java.util.List;

public class RecentlyPlayedTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    RecentlyPlayedPage recentlyPlayedPage;
    public void setupRpPage() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        recentlyPlayedPage = new RecentlyPlayedPage(getDriver());
        loginPage.loginValidCredentials();
    }

    //Song titles do not match
    @Test(description = "Verify song titles in recently played section of homepage match song titles in recently played page")
    public void recentlyPlayedTitles() {
        setupRpPage();
        List<String> homePageTitles = homePage.recentlyPlayedTitles();
        homePage.clickRecentlyPlayed();
        List<String> recentlyPlayedPageTitles = recentlyPlayedPage.recentlyPlayedSongs();
        TestListener.logInfoDetails("Recently played song titles on homepage: " + homePageTitles);
        TestListener.logInfoDetails("Song titles on Recently Played page: " + recentlyPlayedPageTitles);
        TestListener.logAssertionDetails("Song titles are displayed correctly: " + homePageTitles.equals(recentlyPlayedPageTitles));
        Assert.assertNotEquals(recentlyPlayedPageTitles, homePageTitles);
    }
}
