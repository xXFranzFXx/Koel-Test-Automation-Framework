package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;
import util.listeners.TestListener;

import java.util.List;
import java.util.UUID;

public class ProfileTests extends BaseTest {
    ProfilePage profilePage;
    LoginPage loginPage;
    HomePage homePage;
    public ProfileTests() {
        super();
    }

    public void setupProfile() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        profilePage = loginPage.loginValidCredentials().clickAvatar();
    }

    @Test(description = "Update profile name")
    public void changeProfileName()  {
        setupProfile();
        String randomNm = generateRandomName();
        String profileName = profilePage.getProfileName();
        try {
         profilePage
                    .provideCurrentPassword(System.getProperty("koelPassword"))
                    .provideRandomProfileName(randomNm)
                    .clickSave();
            TestListener.logInfoDetails("Updated user name to" + randomNm);
            Assert.assertEquals(profileName, randomNm);
        }catch (Exception e) {
            TestListener.logExceptionDetails("Failed to update user name" + e);
        }
    }
    private String generateRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    @Test(description = "Update theme to In the Pines")
    public void choosePinesTheme() {
        setupProfile();
        try {
            profilePage.clickTheme("pines");
            boolean themeChanged = profilePage.verifyTheme("pines");
            TestListener.logAssertionDetails("Changed theme to pines: " + themeChanged);
            Assert.assertTrue(themeChanged);
        } catch(Exception e) {
            TestListener.logExceptionDetails("Failed to change theme to pines: " + e);
        }
    }
    @Test(description = "Verify functionality of checkboxes")
    public void checkBoxTests() {
        setupProfile();
        List<Boolean> initialState = profilePage.getCheckboxStatus();
        TestListener.logInfoDetails("CheckBox states before clicking: " + profilePage.checkBoxMap());
        try {
            profilePage.clickAllCheckBoxes();
            List<Boolean> currentState = profilePage.getCheckboxStatus();
            TestListener.logInfoDetails("CheckBox states after clicking: " + profilePage.checkBoxMap());
            TestListener.logAssertionDetails("Check boxes can selected/unselected: " + !initialState.equals(currentState));
            Assert.assertNotEquals(initialState, currentState);
        } catch (Exception e) {
            TestListener.logExceptionDetails("Failed to verify checkbox functionality: " + e);
        }
    }
}
