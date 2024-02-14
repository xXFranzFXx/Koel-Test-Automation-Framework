package testcases;

import base.BaseTest;
import org.openqa.selenium.ElementClickInterceptedException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.*;

import java.net.MalformedURLException;

public class CurrentQueueTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    CurrentQueuePage currentQueuePage;

    public CurrentQueueTests() {
        super();
    }

    @BeforeMethod
    @Parameters({"baseURL"})
    public void setup(String baseURL) throws MalformedURLException {
        setupBrowser(baseURL);
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        currentQueuePage = new CurrentQueuePage(getDriver());
        loginPage.loginValidCredentials();
    }

    @Test(description = "Queue page should display total time duration of songs")
    public void checkQueuePageSongs() {
        homePage.clickFooterPlayBtn();
        Assert.assertTrue(currentQueuePage.isSongPlayingCQ());
        Assert.assertTrue(currentQueuePage.checkTotalOrDuration(BasePage.durationRe, currentQueuePage.duration()));
        String total = currentQueuePage.extractTotalOrDuration(BasePage.songTotalRe, currentQueuePage.duration());
        String duration = currentQueuePage.extractTotalOrDuration(BasePage.durationRe, currentQueuePage.duration());
        int actualListSize = currentQueuePage.queueListSize();
        int reported = Integer.parseInt(total);
        Reporter.log("Song Total is" + total, true);
        Reporter.log("Duration is:" + currentQueuePage.duration(), true);
        Reporter.log("counted list size" + currentQueuePage.queueListSize(), true);
        Assert.assertEquals(actualListSize, reported);
    }
}