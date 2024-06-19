package testcases;

import base.BaseTest;
import org.openqa.selenium.ElementClickInterceptedException;
import org.testng.Assert;

import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;
import pages.RegistrationPage;
import util.DataProviderUtil;
import util.ExcelFile;
import util.listeners.TestListener;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    RegistrationPage registrationPage;
    ProfilePage profilePage;
    public LoginTests() {
        super();
    }

    public void setupLogin() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        profilePage = new ProfilePage(getDriver());
        registrationPage = new RegistrationPage(getDriver());
    }
    @Test(enabled = false)
    public void registerNewAccount() {
        try {
            loginPage.clickRegistrationLink();
            registrationPage.registerNewAccount(System.getProperty("koelUser"));
            Assert.assertTrue(registrationPage.getConfirmationMsg());
        } catch (Exception e) {
            TestListener.logExceptionDetails("Unable to login with Excel Data for an unknown reason." + e);
        }
    }
    @Test(description = "log in with newly registered account with testpro.io domain")//(dependsOnMethods = { "registerNewAccount" })
    public void loginWithNewAccount() {
        setupLogin();
        loginPage.provideEmail(System.getProperty("koelUser"))
                .providePassword(System.getProperty("koelPassword"))
                .clickSubmitBtn();
        boolean canLogin = homePage.getUserAvatar();
       TestListener.logAssertionDetails("User has successfully logged in with a new account"+ canLogin);
        Assert.assertTrue(canLogin);
    }

    @Test (description ="log in with new account, update email, log out and attempt to log back in with old email address", dependsOnMethods = { "loginWithNewAccount" })
    public void loginAndUpdateNewAccount() {
        setupLogin();
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
            boolean loggedOut = loginPage.getRegistrationLink();
            TestListener.logAssertionDetails("User has updated email and attempted to log in with old email: " + loggedOut);
            Assert.assertTrue(loggedOut);
        } catch (Exception e) {
            TestListener.logExceptionDetails("There is a problem updating email" + e);
            Assert.assertTrue(homePage.checkForLogoutBtn());
        }
    }

    @Test (description = "Log in with the updated email and update the password, log out, and attempt to log in with old password", dependsOnMethods = { "loginAndUpdateNewAccount"} )
    public void loginWithUpdatedEmailAndUpdatePwd() {
        setupLogin();
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
        boolean loggedOut = loginPage.getRegistrationLink();
        TestListener.logAssertionDetails("User has updated password and logged in using it:  " + loggedOut);
        Assert.assertTrue(loggedOut);
    }

    @Test(description = "Log in success test")
    public void loginSuccessTest() {
        setupLogin();
        loginPage.provideEmail(System.getProperty("koelUser"))
                .providePassword(System.getProperty("koelPassword"))
                .clickSubmitBtn();
        Assert.assertTrue(homePage.getUserAvatar());
        TestListener.logPassDetails("Successful Login");
    }

    @Test(description = "Log in with incorrect password")
    public void loginWrongPasswordTest() {
        setupLogin();
        loginPage.provideEmail(System.getProperty("koelUser"))
                .providePassword("wrongPassword")
                .clickSubmitBtn();
        TestListener.logAssertionDetails("Cannot log in with incorrect password: " + loginPage.getRegistrationLink());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }

    @Test(description = "Log in with incorrect email address")
    public void loginWrongEmailTest() {
        setupLogin();
        loginPage.provideEmail("wrong@wrongmail")
                .providePassword(System.getProperty("koelPassword"))
                .clickSubmitBtn();
        Assert.assertTrue(loginPage.getRegistrationLink());
    }

    @Test(description = "Log in with blank password")
    public void loginEmptyPasswordTest() {
        setupLogin();
        loginPage.provideEmail(System.getProperty("koelUser"))
                .providePassword("")
                .clickSubmitBtn();
        Assert.assertTrue(loginPage.getRegistrationLink());
    }

    @Test(description = "Log in with data read from external source", dataProvider = "LoginData", dataProviderClass = DataProviderUtil.class)
    public void loginWithLoginData(String email, String password) {
        setupLogin();
        try {
            loginPage.provideEmail(email)
                    .providePassword(password)
                    .clickSubmitBtn();
            TestListener.logAssertionDetails("Logged in using logindata: " + homePage.getUserAvatar());
            Assert.assertTrue(homePage.getUserAvatar());
        } catch(Exception e) {
            TestListener.logExceptionDetails("Error logging in with dataProvider: " + e);
            Assert.assertTrue(loginPage.getRegistrationLink());

        }
    }
    @Test(description = "Log in with data read from Excel Sheet", dataProvider = "excel-data", dataProviderClass = ExcelFile.class)
    public void loginWithExcelData(String email, String password){
        setupLogin();
        try{
            loginPage.provideEmail(email)
                    .providePassword(password)
                    .clickSubmitBtn();
            TestListener.logAssertionDetails("Logged in using excel-data: " + homePage.getUserAvatar());
            Assert.assertTrue(homePage.getUserAvatar());
        } catch(Exception e){
            TestListener.logExceptionDetails("Unable to login with Excel Data for an unknown reason." + e);
        }
    }
    @AfterClass(description = "Reset profile back to default newly registered account details after previous test completes")
    public void resetProfile() {
        setupLogin();
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
                TestListener.logAssertionDetails("Restored profile: " + profilePage.notificationPopup());
                Assert.assertTrue(profilePage.notificationPopup());
            } else {
                TestListener.logFailureDetails("Failed to log in, profile was not reset");
                Assert.assertNotEquals(loginUrl, url);
            }
        } catch(ElementClickInterceptedException e) {
           TestListener.logExceptionDetails("Unable to reset profile" + e);
        }
    }

}

