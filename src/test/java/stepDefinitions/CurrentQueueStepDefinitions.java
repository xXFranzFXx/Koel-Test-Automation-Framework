package stepDefinitions;

import base.BaseDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.CurrentQueuePage;
import pages.HomePage;
import pages.LoginPage;

public class CurrentQueueStepDefinitions extends BaseDefinitions {
    LoginPage loginPage;
    HomePage homePage;
    CurrentQueuePage currentQueuePage;
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
        Assert.assertTrue(currentQueuePage.isSongPlayingCQ());
    }

    @And("Total count of songs should be displayed")
    public void totalCountOfSongsShouldBeDisplayed() {
    }

    @And("Duration of count of all songs should be displayed")
    public void durationOfCountOfAllSongsShouldBeDisplayed() {
    }

    @And("ID, Title, Artist, ALbum and Time should be displayed")
    public void idTitleArtistALbumAndTimeShouldBeDisplayed() {
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
