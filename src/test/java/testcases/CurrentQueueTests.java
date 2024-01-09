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
    }
    @Test(description = "Queue page should display total time duration of songs")
    public void checkQueuePageSongs() {
        loginPage.loginValidCredentials();
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

    public static class LoginTests extends BaseTest {


        LoginPage loginPage;
        HomePage homePage;
        RegistrationPage registrationPage;
        ProfilePage profilePage;
        public LoginTests() {
            super();
        }
        @BeforeClass
        public void setEnv() {
            loadEnv();
        }

        @BeforeMethod
        @Parameters({"baseURL"})
        public void setup(String baseURL) throws MalformedURLException {
            setupBrowser(baseURL);
            loginPage = new LoginPage(getDriver());
            homePage = new HomePage(getDriver());
            registrationPage = new RegistrationPage(getDriver());

        }
    //    @Test
        public void registerNewAccount() {
            try {
                loginPage.clickRegistrationLink();
                registrationPage.registerNewAccount(System.getProperty("koelUser"));
                Assert.assertTrue(registrationPage.getConfirmationMsg());
            } catch (Exception e) {
                Reporter.log("Unable to login with Excel Data for an unknown reason." + e);
            }
        }
        @Test (description = "log in with newly registered account with testpro.io domain")//(dependsOnMethods = { "registerNewAccount" })
        public void loginWithNewAccount() {
            loginPage.provideEmail(System.getProperty("koelUser"))
                    .providePassword(System.getProperty("koelPassword"))
                    .clickSubmitBtn();
            Assert.assertTrue(homePage.getUserAvatar());
            Reporter.log("User has successfully logged in with a new account", true);
        }


        @Test (description ="log in with new account, update email, log out and attempt to log back in with old email address", dependsOnMethods = { "loginWithNewAccount" })
        public void loginAndUpdateNewAccount() {

            loginPage.provideEmail(System.getProperty("koelUser"))
                    .providePassword(System.getProperty("koelPassword"))
                    .clickSubmitBtn();
            try {
                profilePage
                        .provideNewEmail(System.getProperty("koelNewUser"))
                        .provideCurrentPassword(System.getProperty("koelPassword"))
                        .clickSaveButton()
                        .clickLogout();
                loginPage.provideEmail(System.getProperty("koelUser"))
                        .providePassword(System.getProperty("koelPassword"))
                        .clickSubmitBtn();
                Assert.assertTrue(loginPage.getRegistrationLink());
                Reporter.log("User has updated email and attempted to log in with old email", true);
            } catch (Exception e) {
                Reporter.log("There is a problem updating email" + e, true);
                Assert.assertTrue(homePage.checkForLogoutBtn());
            }
        }

        @Test (description = "Log in with the updated email and update the password, log out, and attempt to log in with old password", dependsOnMethods = { "loginAndUpdateNewAccount"} )
        public void loginWithUpdatedEmailAndUpdatePwd() {
            loginPage.provideEmail(System.getProperty("koelNewUser"))
                    .providePassword(System.getProperty("koelPassword"))
                    .clickSubmitBtn();
            profilePage
                    .provideNewPassword(System.getProperty("updatedPassword"))
                    .provideCurrentPassword(System.getProperty("koelPassword"))
                    .clickSaveButton()
                    .clickLogout();
            loginPage.provideEmail(System.getProperty("koelNewUser"))
                    .providePassword(System.getProperty("koelPassword"))
                    .clickSubmitBtn();
            Assert.assertTrue(loginPage.getRegistrationLink());
            Reporter.log("User successfully updated password and logged in using it", true);

        }

        @Test(description = "Log in success test", groups = { "Login" })
        public void loginSuccessTest() {
            loginPage.provideEmail(System.getProperty("koelUser"))
                    .providePassword(System.getProperty("koelPassword"))
                    .clickSubmitBtn();
            Assert.assertTrue(homePage.getUserAvatar());
        }

        @Test(description = "Log in with incorrect password", groups = { "Login" })
        public void loginWrongPasswordTest() {
            loginPage.provideEmail(System.getProperty("koelUser"))
                    .providePassword("wrongPassword")
                    .clickSubmitBtn();
            Assert.assertTrue(loginPage.getRegistrationLink());
        }

        @Test(description = "Log in with incorrect email address", groups = { "Login" })
        public void loginWrongEmailTest() {
            loginPage.provideEmail("wrong@wrongmail")
                    .providePassword(System.getProperty("koelPassword"))
                    .clickSubmitBtn();
            Assert.assertTrue(loginPage.getRegistrationLink());
        }

        @Test(description = "Log in with blank password", groups = { "Login" })
        public void loginEmptyPasswordTest() {
            loginPage.provideEmail(System.getProperty("koelUser"))
                    .providePassword("")
                    .clickSubmitBtn();
            Assert.assertTrue(loginPage.getRegistrationLink());
        }

        @Test(description = "Log in with data read from external source", dataProvider = "LoginData")
        public void loginWithLoginData(String email, String password) {
            try {
                loginPage.provideEmail(email)
                        .providePassword(password)
                        .clickSubmitBtn();
                    Assert.assertTrue(homePage.getUserAvatar());
                    Reporter.log("Logged in using logindata", true);

            } catch(Exception e) {

                Assert.assertTrue(loginPage.getRegistrationLink());
                Reporter.log("Invalid login data, check username or password." + e, true);
            }
        }
        @Test(description = "Log in with data read from Excel Sheet", dataProvider = "excel-data")
        public void loginWithExcelData(String email, String password){
            try{
                loginPage.provideEmail(email)
                        .providePassword(password)
                                .clickSubmitBtn();
                Assert.assertTrue(homePage.getUserAvatar());
                Reporter.log("Logged in using excel-data", true);
            } catch(Exception e){
                Reporter.log("Unable to login with Excel Data for an unknown reason." + e, true);
            }
        }
    //    @AfterClass(description = "Reset profile back to default newly registered account details after previous test completes", dependsOnMethods = { "loginWithUpdatedEmailAndUpdatePwd" })
        public void resetProfile() {
            String url = "https://qa.koel.app/#!/home";
            String loginUrl = driver.getCurrentUrl();
            try {
                loginPage.provideEmail(System.getProperty("koelNewUser"))
                        .providePassword(System.getProperty("updatedPassword"))
                        .clickSubmitBtn();
                if(loginUrl.equals(url)) {
                    profilePage
                            .provideNewEmail(System.getProperty("koelUser"))
                            .provideNewPassword(System.getProperty("koelPassword"))
                            .provideCurrentPassword(System.getProperty("updatedPassword"))
                            .clickSaveButton();
                    Assert.assertTrue(profilePage.notificationPopup());
                    Reporter.log("Restored profile", true);
                } else {
                    Assert.assertNotEquals(loginUrl, url);
                    Reporter.log("Failed to log in, profile was not reset", true);
                }
            } catch(ElementClickInterceptedException e) {
                Reporter.log("Unable to reset profile" + e, true);
            }
        }

    }
}
