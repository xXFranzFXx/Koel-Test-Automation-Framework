package stepDefinitions;

import base.BaseDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import pages.BasePage;
import pages.CurrentQueuePage;
import pages.HomePage;
import pages.LoginPage;

public class CurrentQueueStepDefinitions extends BaseDefinitions {
    LoginPage loginPage;
    HomePage homePage;
    CurrentQueuePage currentQueuePage;
    private static String durationRe = "[^\\W•]+([1-9][0-99]+|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])";
    private static String songTotalRe = "^\\d{1,}[^\\W•]";
    @Given("User has songs playing")
    public void userHasSongsPlaying() {
        loginPage = new LoginPage(getDriver());
        loginPage.loginValidCredentials();
        homePage = new HomePage(getDriver());
        homePage.clickFooterPlayBtn();
    }

    @When("User navigates to Current Queue page")
    public void userNavigatesToCurrentQueuePage() {
        homePage = new HomePage(getDriver());
        homePage.clickCurrentQueue();
    }

    @Then("Currently playing songs should be listed")
    public void currentlyPlayingSongsShouldBeListed() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        String total = currentQueuePage.extractTotalOrDuration(BasePage.songTotalRe, currentQueuePage.duration());
        int actualListSize = currentQueuePage.queueListSize();
        int reported = Integer.parseInt(total);
        Assert.assertEquals(reported, actualListSize);
    }

    @And("Total count of songs should be displayed")
    public void totalCountOfSongsShouldBeDisplayed() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        Assert.assertTrue(currentQueuePage.checkTotalOrDuration(songTotalRe, currentQueuePage.duration()));
    }

    @And("Duration of count of all songs should be displayed")
    public void durationOfCountOfAllSongsShouldBeDisplayed() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        Assert.assertTrue(currentQueuePage.checkTotalOrDuration(durationRe, currentQueuePage.duration()));
    }

    @And("ID, Title, Artist, ALbum and Time should be displayed")
    public void idTitleArtistALbumAndTimeShouldBeDisplayed() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        Assert.assertTrue(currentQueuePage.findSongInfo());
    }

    @Given("User clicks on {string}")
    public void userClicksOn(String arg0) {
    }

    @Then("User will navigate to {string} page")
    public void userWillNavigateToPage(String arg0) {
    }

    @When("User double clicks on a song")
    public void userDoubleClicksOnASong() {

    }

    @Then("User will navigate to Current Queue page")
    public void userWillNavigateToCurrentQueuePage() {
    }

    @When("User clicks {string} button")
    public void userClicksShuffleAllButton() {
    }

    @Then("Currently playing songs will be shuffled")
    public void currentlyPlayingSongsWillBeShuffled() {
    }

    @Then("Songs will disappear")
    public void songsWillDisappear() {
    }

    @And("the message {string} will appear")
    public void theMessageWillAppear(String arg0) {
    }
}
