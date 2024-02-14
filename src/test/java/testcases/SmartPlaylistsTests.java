package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import util.RandomString;
import util.TestUtil;
import util.listeners.TestListener;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SmartPlaylistsTests extends BaseTest {
    LoginPage loginPage;
    HomePage homePage;
    Map<String, String> dataMap = new HashMap<>();
    @BeforeMethod
    public void setup() throws MalformedURLException {
        setupBrowser(System.getProperty("baseURL"));
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        loginPage.loginValidCredentials();
    }
    private String generatePlaylistName(int nameLength) {
        String name = RandomString.getAlphaNumericString(nameLength);
        TestListener.logInfoDetails("Smart playlist name: " + name);
        return name;
    }
    @Test(description = "User can create a smart playlist with one rule and verify related songs appear")
    public void createSmartPlaylist() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectOperatorOption("contains")
                .enterSmartListTextCriteria("dark")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist with songs matching rule criteria: " + !homePage.checkSmartListEmpty());
        Assert.assertFalse(homePage.checkSmartListEmpty(), "No songs match the rule criteria");
    }
    @Test(description = "Verify empty smart playlist is created when no songs match rule criteria")
    public void createSmartPlaylistNoMatches() {
        String playlist = generatePlaylistName(5);
        String expectedText = "No songs match the playlist's criteria.";
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .enterSmartListTextCriteria("dark")
                .clickSaveSmartList();
        Object[] expected = {true, expectedText};
        Object[] actual = {homePage.checkEmptySmartListIcon(), homePage.emptySmartListMessage()};
        TestListener.logAssertionDetails("Created smart playlist with no songs matching rule criteria: " + expectedText.equalsIgnoreCase(homePage.emptySmartListMessage()));
        Assert.assertEquals(expected, actual, "There are songs in the smart playlist");
    }
    @Test(description = "Create a smart playlist with group rules")
    public void createSmartPlaylistGroupRules() {
        String playlist = generatePlaylistName(5);
        String[] text = {"dark", "grav"};
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .clickGroupRuleBtn()
                .enterGroupRulesText(text)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist with group rules: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist with group rules");
    }
    @Test(description = "Create a smart playlist based on 'Plays' rule criteria")
    public void createSmartPlaylistByPlay() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Plays")
                .enterSmartListIntCriteria(3)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'Plays' model: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'Plays' model");
    }
    @Test(description= "Create a smart playlist with name has length of one character")
    public void createSmartPlaylistShortName() {
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName("a")
                .enterSmartListTextCriteria("dark")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist with name that has one-character length: " + homePage.smartlistAddedToMenu("a"));
        Assert.assertTrue(homePage.smartlistAddedToMenu("a") , "Unable to create a new smart playlist with a name containing a single character");
    }
    @Test(description = "Create a smart playlist with name that exceeds 256-character length")
    public void createSmartPlaylistLongName() {
        String longName = generatePlaylistName(257);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(longName)
                .enterSmartListTextCriteria("dark")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist with name exceeding 256-character length max: " + homePage.smartlistAddedToMenu(longName));
        Assert.assertTrue(homePage.smartlistAddedToMenu(longName), "Unable to create a new smart playlist with name exceeding 256 character-length maximum");

    }
    @Test(description = "Create a smart playlist based on 'Date Added' rule criteria")
    public void createSmartPlaylistByDateAdded(){
        String playlist = generatePlaylistName(5);
        String currentDate = TestUtil.getDate();
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Date Added")
                .enterSmartListDateCriteria(currentDate)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'Date Added' model: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'Date Added' model");
    }
    @Test(description = "Verify functionality of 'Cancel' button when creating a new smart playlist")
    public void testCancelButton() {
        String playlist = generatePlaylistName(5);
        String homePageUrl = "https://qa.koel.app/#!/home";
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .clickSmartListCancelBtn();
        TestListener.logAssertionDetails("Verified functionality of 'Cancel' button: " + homePageUrl.equalsIgnoreCase(getDriver().getCurrentUrl()));
        Assert.assertEquals(homePageUrl, getDriver().getCurrentUrl(), "Unable to verify functionality of 'Cancel' button");
    }
    @Test(description = "Create a new smart playlist using the operator 'is not' in the rule criteria")
    public void titleIsNot() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectOperatorOption("is not")
                .enterSmartListTextCriteria("dark")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'is not' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'is not' operator");
    }
    @Test(description = "Create a new smart playlist using the operator 'does not contain' in the rule criteria")
    public void titleDoesNotContain() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectOperatorOption("does not contain")
                .enterSmartListTextCriteria("dark")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'does not contain' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'does not contain' operator");
    }
    @Test(description = "Create a new smart playlist using the operator 'begins with' in the rule criteria")
    public void titleBeginsWith() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectOperatorOption("begins with")
                .enterSmartListTextCriteria("a")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'begins with' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'begins with' operator");
    }
    @Test(description = "Create a new smart playlist using the operator 'ends with' in the rule criteria")
    public void titleEndsWith() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectOperatorOption("ends with")
                .enterSmartListTextCriteria("a")
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'ends with' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'ends with' operator");
    }
    @Test(description = "Create a new smart playlist using the operator 'is greater than' in the rule criteria")
    public void playsIsGreaterThan() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Plays")
                .selectOperatorOption("is greater than")
                .enterSmartListIntCriteria(3)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'is greater than' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'is greater than' operator");
    }
    @Test(description = "Create a new smart playlist using the operator 'is less than' in the rule criteria")
    public void playsIsLessThan() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Plays")
                .selectOperatorOption("is less than")
                .enterSmartListIntCriteria(3)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'is less than' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'is less than' operator");
    }
    @Test(description = "Create a new smart playlist using the operator 'is between' in the rule criteria")
    public void playsIsBetween() {
        String playlist = generatePlaylistName(5);
        int[] plays = {2, 5};
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Plays")
                .selectOperatorOption("is between")
                .enterIntCriteria(plays)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'is between' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'is between' operator");
    }
    @Test(description = "Verify user cannot create a new smart playlist with blank name")
    public void blankPlaylistName() {
        String validationMessage = "Please fill out this field.";
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Verified smart playlist with blank name cannot be created: " + validationMessage.equalsIgnoreCase(homePage.getNameInputValidationMsg()));
        Assert.assertEquals(validationMessage, homePage.getNameInputValidationMsg(), "Incorrect or missing form validation message");
    }
    @Test(description = "Verify user cannot create a new smart playlist with no rules criteria")
    public void blankRuleCriteria() {
        String playlist = generatePlaylistName(5);
        String validationMessage = "Please fill out this field.";
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Verified smart playlist with blank rule criteria cannot be created: " + homePage.isEditModalVisible());
        Assert.assertTrue(homePage.isEditModalVisible());
    }
    @Test(description = "Create a smart playlist based on 'Plays' rule with a negative integer input criteria")
    public void negativeNumberCriteria() {
        String playlist = generatePlaylistName(5);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Plays")
                .enterSmartListIntCriteria(-3)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'Plays' model and negative integer input rule criteria: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist with negative integer rule criteria");
    }
    @Test(description = "Create a smart playlist based on 'in the last' rule criteria")
    public void inTheLastCriteria(){
        String playlist = generatePlaylistName(5);
        String currentDate = TestUtil.getDate();
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Date Added")
                .selectOperatorOption("in the last")
                .enterSmartListIntCriteria(5)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'in the last' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'in the last' operator");
    }
    @Test(description = "Create a smart playlist based on 'not in the last' rule criteria")
    public void notInTheLastCriteria(){
        String playlist = generatePlaylistName(5);
        String currentDate = TestUtil.getDate();
        homePage.clickCreateNewPlaylist()
                .contextMenuNewSmartlist()
                .enterSmartListName(playlist)
                .selectModelOption("Date Added")
                .selectOperatorOption("not in the last")
                .enterSmartListIntCriteria(5)
                .clickSaveSmartList();
        TestListener.logAssertionDetails("Created smart playlist using the 'not in the last' operator: " + homePage.smartlistAddedToMenu(playlist));
        Assert.assertTrue(homePage.smartlistAddedToMenu(playlist), "Unable to create a new smart playlist using the 'not in the last' operator");
    }
    @Test(description = "Verify existing smart playlist name can be edited")
    public void editListName() throws SQLException, ClassNotFoundException {
        String newName = generatePlaylistName(6);
        homePage.cmEditFirstSmartPl()
                .editSmartPlName(newName)
                .clickSaveSmartList();
        dataMap.put("editedName", newName);
        TestListener.logInfoDetails("Smart playlist name after: " + homePage.getFirstSmartPlName());
        TestListener.logAssertionDetails("User can edit smart playlist name: " + homePage.getFirstSmartPlName().contains(newName));
        Assert.assertTrue(homePage.getFirstSmartPlName().contains(newName), "Unable to edit smart playlist name");
    }
    @Test(description = "Delete all smart playlists", dependsOnMethods = {"titleIsNot"})
    public void deleteAllSmartPl() {
        homePage.deleteAllPlaylists();
        Assert.assertTrue(homePage.playlistsEmpty());
    }
}
