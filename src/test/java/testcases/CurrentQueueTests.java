package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import util.listeners.TestListener;

public class CurrentQueueTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    CurrentQueuePage currentQueuePage;

    public CurrentQueueTests() {
        super();
    }

    public void setupCQ() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        currentQueuePage = new CurrentQueuePage(getDriver());
        loginPage.loginValidCredentials();
    }

    @Test(description = "Queue page should display total time duration of songs")
    public void checkQueuePageSongs() {
        setupCQ();
        homePage.clickFooterPlayBtn();
        String total = currentQueuePage.extractTotalOrDuration(BasePage.songTotalRe, currentQueuePage.duration());
        String duration = currentQueuePage.extractTotalOrDuration(BasePage.durationRe, currentQueuePage.duration());
        int actualListSize = currentQueuePage.queueListSize();
        int reported = Integer.parseInt(total);
        TestListener.logInfoDetails("Song Total is" + total);
        TestListener.logInfoDetails("Duration is:" + currentQueuePage.duration());
        TestListener.logInfoDetails("counted list size" + currentQueuePage.queueListSize());
        Assert.assertTrue(currentQueuePage.checkTotalOrDuration(BasePage.durationRe, currentQueuePage.duration()));
    }
}