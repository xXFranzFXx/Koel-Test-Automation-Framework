package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;
import util.listeners.TestListener;

import java.util.UUID;

/**
 * Story:
 * As a user, I want to be able to log out of my account
 * Acceptance Criteria:
 * 1. 'Log student out' button should be present on the homepage, next to 'Profile' button
 * 2. User should be able to log out after successful login
 * 3. User should be navigated to the Login page after logging out
 * 4. User should be able to log out after updating email and password
 */

public class LogoutTests extends BaseTest {
    private String generateRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    LoginPage loginPage;
    HomePage homePage;
    ProfilePage profilePage;
    public LogoutTests() {
        super();
    }
    public void setupLogout(){
        homePage = new HomePage(getDriver());
        loginPage = new LoginPage(getDriver());
        profilePage = new ProfilePage(getDriver());
    }
    @Test(description = "Log in and verify visibility of logout button, then log out")
    public void useLogoutButton() {
        setupLogout();
        loginPage.loginValidCredentials();
        homePage.clickLogoutButton();
        TestListener.logAssertionDetails("Successfully logged out after logging in: " + loginPage.getRegistrationLink());
        Assert.assertTrue(loginPage.getRegistrationLink(), "Error logging out.");
    }
    @Test(enabled=false, dependsOnMethods = { "useLogoutButton" }, description = "Update username and password then logout and verify navigation back to login screen")
    public void logoutAfterProfileUpdate() {
        setupLogout();
        try {
            loginPage.loginValidCredentials();
            homePage.clickAvatar();
            String randomNm = generateRandomName();
            String password = System.getProperty("koelPassword");
            String profileName = profilePage.getProfileName();
            profilePage
                    .provideNewPassword(System.getProperty("koelPassword"))
                    .provideRandomProfileName(randomNm)
                    .provideCurrentPassword(password);
            Assert.assertTrue(profilePage.clickSave(), "Error saving profile name.");
        } catch (Exception e) {
            Reporter.log("Unable to update profile name." + e.getLocalizedMessage(), true);
        }
        try {
        profilePage.clickLogout();
        Assert.assertTrue(loginPage.getRegistrationLink(), "Error logging out.");
        TestListener.logAssertionDetails("User can log out after updating profile name: " + loginPage.getRegistrationLink());
        Reporter.log("User has logged out after updating username and password and redirected to login page", true);
    } catch (Exception e) {
            Reporter.log("Unable to log out after updating profile." + e.getLocalizedMessage(), true);
            }
        }
}
