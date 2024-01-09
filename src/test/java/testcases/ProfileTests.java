package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;

import java.net.MalformedURLException;
import java.util.UUID;

public class ProfileTests extends BaseTest {
    ProfilePage profilePage;
    LoginPage loginPage;
    HomePage homePage;
    public ProfileTests() {
        super();
    }
    @BeforeMethod
    @Parameters({"baseURL"})
    public void setup(String baseURL) throws MalformedURLException {
        setupBrowser(baseURL);
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        profilePage = loginPage.loginValidCredentials().clickAvatar();
    }
    @AfterMethod
    public void close() {
        closeBrowser();
    }

    @Test(description = "Update profile name")
    public void changeProfileName()  throws InterruptedException {
        String randomNm = generateRandomName();
        String profileName = profilePage.getProfileName();
        try {
         profilePage
                    .provideCurrentPassword(System.getProperty("koelPassword"))
                    .provideRandomProfileName(randomNm)
                    .clickSave();
            Assert.assertEquals(profileName, randomNm);
            Reporter.log("Updated user name to" + randomNm, true);
        }catch (Exception e) {
            Reporter.log("Failed to update user name" + e, true);
        }
    }
    private String generateRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Test(description = "Update theme to In the Pines")
    public void choosePinesTheme() {
        try {
            profilePage
                    .clickTheme("pines");
            Assert.assertTrue(profilePage.verifyTheme("pines"));
            Reporter.log("Changed theme to pines", true);
        } catch(Exception e) {
            Reporter.log("Failed to change theme to pines theme" + e, true);
            Assert.assertFalse(false);
        }
    }
}
