package stepDefinitions;

import base.BaseDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.*;

public class CurrentQueueStepDefinitions extends BaseDefinitions {
    LoginPage loginPage;
    HomePage homePage;
    ArtistsPage artistsPage;
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
        homePage = new HomePage(getDriver());
        homePage.navigateFromSideMenu(arg0);
    }

    @Then("User will navigate to {string} page")
    public void userWillNavigateToPage(String arg0) {
        Assert.assertEquals(getDriver().getCurrentUrl(), arg0);
    }

    @When("User double clicks on a song")
    public void userDoubleClicksOnASong() {
        homePage = new HomePage(getDriver());
        artistsPage = new ArtistsPage(getDriver());
        homePage.clickArtists();
        artistsPage.playFirstArtist();
    }

    @Then("User will navigate to Current Queue page")
    public void userWillNavigateToCurrentQueuePage() {
        Assert.assertEquals(getDriver().getCurrentUrl(), "https://qa.koel.app/#!/queue");
    }

    @When("User clicks {string} button")
    public void userClicksShuffleAllButton() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        currentQueuePage.clickShuffleBtn();
    }

    @Then("Currently playing songs will be shuffled")
    public void currentlyPlayingSongsWillBeShuffled() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        String current = currentQueuePage.getCurrentlyPlaying();
        currentQueuePage.clickShuffleBtn();
        Assert.assertNotEquals(current, currentQueuePage.getCurrentlyPlaying());
    }
    @When("User clicks 'Clear' button")
    public void userClicksClearButton() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        currentQueuePage.clickClearBtn();
    }

    @Then("Songs will disappear")
    public void songsWillDisappear() {
        currentQueuePage = new CurrentQueuePage(getDriver());
        Assert.assertFalse(currentQueuePage.queueListExists());
    }

    @And("the message {string} will appear")
    public void theMessageWillAppear(String arg0) {
        currentQueuePage = new CurrentQueuePage(getDriver());
        Assert.assertTrue(currentQueuePage.getEmptyListText().contains(arg0));
    }
}
