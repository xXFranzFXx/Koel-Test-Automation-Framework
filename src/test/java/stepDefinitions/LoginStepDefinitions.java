package stepDefinitions;


import base.BaseDefinitions;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;
import pages.ProfilePage;

import java.net.MalformedURLException;


public class LoginStepDefinitions  extends BaseDefinitions {
    LoginPage loginPage;
    ProfilePage profilePage;
    @Before
    public void setup() throws MalformedURLException {
        setupBrowser();
    }
    @After
    public void close() {
        closeBrowser();
    }

    @Then("User should navigate to home page")
    public void checkHomePage() {
    }

    @When("User enters email {string}")
    public void userEntersEmail(String email) {
        loginPage = new LoginPage(getDriver());
        loginPage.enterEmail(checkString(email));
    }

    @And("User enters password {string}")
    public void userEntersPassword(String password) {
        loginPage = new LoginPage(getDriver());
        loginPage.providePassword(checkString(password));
    }

    @And("User clicks submit")
    public void userClicksSubmit() {
        loginPage = new LoginPage(getDriver());
        loginPage.clickSubmitBtn();
    }

    @Then("User should still be on Login page")
    public void userShouldStillBeOnLoginPage() {
        loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }

    @Given("User logs in")
    public void userLogsIn() {
        loginPage = new LoginPage(getDriver());
        loginPage.loginValidCredentials();
    }

    @When("User provides current password {string}")
    public void userProvidesCurrentPassword(String password) {
        profilePage = new ProfilePage(getDriver());
        profilePage.provideCurrentPassword(checkString(password));
    }

    @And("User provides new email address {string}")
    public void userProvidesNewEmailAddress(String newEmail) {
        profilePage = new ProfilePage(getDriver());
        profilePage.provideNewEmail(checkString(newEmail));
    }

    @And("User provides new password {string}")
    public void userProvidesNewPassword(String newPasswd) {
        profilePage = new ProfilePage(getDriver());
        profilePage.provideNewPassword(checkString(newPasswd));
    }

    @Given("User is on login page")
    public void userIsOnLoginPage() {
        loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }
}
