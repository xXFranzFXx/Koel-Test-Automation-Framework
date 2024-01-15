package stepDefinitions;

import base.BaseDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.HomePage;
import pages.ProfilePage;

public class ProfileStepDefinitions extends BaseDefinitions{
    ProfilePage profilePage;
    HomePage homePage;

    @When("User clicks on a theme {string}")
    public void userClicksOnATheme(String theme) {
        profilePage = new ProfilePage(getDriver());
        profilePage.clickTheme(theme);
    }
    @Then("The profile theme will be updated to {string}")
    public void theProfileThemeWillBeUpdatedTo(String theme) {
        profilePage = new ProfilePage(getDriver());
        Assert.assertTrue(profilePage.verifyTheme(theme));
    }

    @And("User navigates to profile page")
    public void userNavigatesToProfilePage() {
        homePage = new HomePage(getDriver());
        homePage.clickAvatar();
    }

    @Then("User receives error message")
    public void userReceivesErrorMessage() {
        profilePage = new ProfilePage(getDriver());
        Assert.assertEquals("true", profilePage.invalidEmailMsg());
    }


    @And("User clicks save")
    public void userClicksSave() {
        profilePage = new ProfilePage(getDriver());
        profilePage.moveToSaveAndClick();
    }
}
