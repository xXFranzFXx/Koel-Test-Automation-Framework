package stepDefinitions;

import base.BaseDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;

import java.util.UUID;


public class LogoutStepDefinitions extends BaseDefinitions {
    LoginPage loginPage;
    ProfilePage profilePage;
    HomePage homePage;
    private static String generateRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    private static final String newName = generateRandomName();


    @And("User is logged in")
    public void login() {

            loginPage = new LoginPage(getDriver());
            loginPage.loginValidCredentials();


        }
    @And("Logout button is visible next to profile pic")
    public void logoutBtnVisible() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.checkForLogoutBtn());
    }


    @When("User clicks logout button")
    public void userClicksLogoutButton() {
        profilePage = new ProfilePage(getDriver());
        profilePage.clickLogout();
    }

    @Then("User is logged out and navigates back to login screen")
    public void userIsLoggedOutAndNavigatesBackToLoginScreen() {
        loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }

    @When("User clicks profile pic")
    public void userClicksProfilePic() {
        homePage = new HomePage(getDriver());
        homePage.clickAvatar();
    }

    @Then("Profile page is opened")
    public void profilePageIsOpened() {
        profilePage = new ProfilePage(getDriver());
        Assert.assertEquals(getDriver().getCurrentUrl(), profileUrl);
    }

    @When("User enters current password")
    public void userEntersCurrentPassword() {
        profilePage = new ProfilePage(getDriver());
        profilePage.provideCurrentPassword("te$t$tudent1");
    }

    @And("User enters updated name")
    public void userEntersUpdatedName() {
        profilePage = new ProfilePage(getDriver());
        profilePage.provideRandomProfileName(newName);
    }


    @And("User clicks save button")
    public void userClicksSaveButton() {
        profilePage = new ProfilePage(getDriver());
        profilePage.clickSave();

    }

    @Then("Success message is displayed")
    public void successMessageIsDisplayed() {
        profilePage = new ProfilePage(getDriver());
        Assert.assertTrue(profilePage.notificationPopup());
    }

    @When("Success message disappears")
    public void successMessageDisappears() {
        profilePage = new ProfilePage(getDriver());
        Assert.assertTrue(profilePage.notificationHasDisappeared());
    }

    @Then("User navigates back to login screen")
    public void userNavigatesBackToLoginScreen() {
        loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }

}
