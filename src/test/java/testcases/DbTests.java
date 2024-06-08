package testcases;

import base.BaseTest;
import db.KoelDb;
import db.KoelDbActions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import util.DataProviderUtil;

import util.TestUtil;
import util.dbUtils.DbTestUtil;
import util.listeners.TestListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbTests extends BaseTest {
    LoginPage loginPage;
    AllSongsPage allSongsPage;
    HomePage homePage;
    ArtistsPage artistsPage;
    RegistrationPage registrationPage;
    ResultSet rs;
    List<String> dataList = new ArrayList<>();
    Map<String, String> dataMap = new HashMap<>();
    Map<String, Object> rsMap = new HashMap<>();

    private final int maxLength = 256;
    public void setupKoel() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        artistsPage = new ArtistsPage(getDriver());
        allSongsPage = new AllSongsPage(getDriver());
        loginPage.loginValidCredentials();
    }
    @BeforeMethod
    public void openDb() throws SQLException, ClassNotFoundException {
        KoelDb.initializeDb();
    }
    @AfterMethod
    public void shutDown() throws SQLException {
        KoelDb.closeDatabaseConnection();
    }

    @Test(description = "Verify total song count displayed in app matches the total song count from the database")
    public void verifyTotalSongTracks() throws SQLException {
        setupKoel();
        KoelDbActions koelDbActions = new KoelDbActions();
        allSongsPage.navigateToAllSongs();
        rs = koelDbActions.totalSongCount();
        if(rs.next()) {
            int count = rs.getInt("count");
            int totalSongsInApp = Integer.parseInt(allSongsPage.getSongTotalFromHeader());
            TestListener.logInfoDetails("Total song tracks displayed on All Songs page header: " + totalSongsInApp);
            TestListener.logInfoDetails("Total song tracks in database: " + count);
            TestListener.logAssertionDetails("Total songs in app matches total songs in database: " + (count == totalSongsInApp));
            Assert.assertEquals(count, totalSongsInApp, "Total song count displayed in app does not match the total song count from database");
        }
    }
    @Test(description = "Verify the total duration displayed in All Songs Page matches the sum of all durations in database")
    public void verifyTotalDuration() throws SQLException{
        setupKoel();
        allSongsPage.navigateToAllSongs();
        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.totalDuration();
        if(rs.next()) {
            int durationFromDb = rs.getInt("duration");
            String convertedDurationFromDb = TestUtil.convertSecondToHHMMSSString(durationFromDb);
            String durationInApp = allSongsPage.getDurationFromHeader();
            TestListener.logInfoDetails("Total duration displayed on All Songs page: " + durationInApp);
            TestListener.logInfoDetails("Total duration from database: " + convertedDurationFromDb);
            TestListener.logAssertionDetails("Total duration in app matches total duration in database: " + durationInApp.equals(convertedDurationFromDb));
            Assert.assertEquals(convertedDurationFromDb, durationInApp, "Duration displayed in app does not match total durtion in database");
        }
    }

    @Test(description = "User can create a playlist", dataProvider="PlaylistData", dataProviderClass = DataProviderUtil.class)
    public void createPlaylist(String playlist) {
        setupKoel();
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName(playlist);
        Assert.assertTrue(homePage.playlistAddedToMenu(playlist));
    }
    @Test(description = "Verify user can create playlists with duplicate names, check database for multiple playlists with same name", dataProvider="PlaylistData", dataProviderClass = DataProviderUtil.class, dependsOnMethods = {"createPlaylist"})
    public void createPlaylistDuplicateName(String playlist) throws SQLException {
        setupKoel();
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName(playlist);
        int duplicateCount = DbTestUtil.countDuplicateNames(System.getProperty("koelUser"), playlist);
        Assert.assertTrue(DbTestUtil.duplicateCondition(duplicateCount), "No duplicate playlist names were found");
    }
    @Test(description = "Add a song to a playlist", dependsOnMethods = {"createPlaylist"})
    @Parameters({"song"})
    public void addSongToPlaylist(String song) throws SQLException {
                setupKoel();
                homePage.searchSong(song)
                .clickViewAllButton()
                .clickFirstSearchResult()
                .clickGreenAddToBtn()
                .selectPlaylistToAddTo("playlist");
        Assert.assertTrue(DbTestUtil.checkDatabaseForSongInPlaylist(System.getProperty("koelUser"), song), "Playlist song could not be found");
    }

    @Test(description = "delete all playlists",dependsOnMethods = {"addSongToPlaylist"})
    public void deleteAllPlaylists(){
        setupKoel();
        homePage.deleteAllPlaylists();
        Assert.assertTrue(homePage.playlistsEmpty(), "Could not delete all playlists");
    }
    @Test(description = "Create a playlist with over 256 characters and check if it is saved to the database")
    public void createLongPlaylistName() throws SQLException {
        setupKoel();
        String playlistName = generatePlaylistName(maxLength);
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName(playlistName)
                        .wait(4);

        Assert.assertTrue(DbTestUtil.checkDatabaseForPlaylist(System.getProperty("koelUser"), playlistName), "Playlist not found in database");
        Assert.assertTrue(homePage.playlistAddedToMenu(playlistName), "Playlist was not successfully created");
    }
    @Test(description = "Create a playlist with a name containing one character and verify it is in the database", dataProvider = "PlaylistData", dataProviderClass = DataProviderUtil.class)
    public void createOneCharacterPlaylistName(String playlistName) throws SQLException {
        setupKoel();
        homePage.clickCreateNewPlaylist()
                .contextMenuNewPlaylist()
                .enterPlaylistName(playlistName);
        Assert.assertTrue(DbTestUtil.checkDatabaseForPlaylist(System.getProperty("koelUser"), playlistName), "Playlist not found in database");
        Assert.assertTrue(homePage.playlistAddedToMenu(playlistName), "Playlist was not successfully created");
    }
    @Test(description = "Check artists that are displayed in app and compare with artists displayed in db")
    public void checkDbForArtists() throws SQLException{
        setupKoel();
        artistsPage.navigateToArtistsPage();
        List<String> names = artistsPage.getArtistsNames();
        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.checkArtistsInDb();
        while (rs.next()) {
            String name = rs.getString("name");
            dataList.add(name);
        }
        List<String> differences = new ArrayList<>(dataList);
        differences.removeAll(names);
        TestListener.logInfoDetails("Artist names found in app: " + names);
        TestListener.logInfoDetails("Artist names found in db: " + dataList);
        TestListener.logRsDetails("Database contains some artists that are not shown in app: " + !differences.isEmpty());
        TestListener.logAssertionDetails("Artists names are stored correctly: " + (names.size()==dataList.size()));
        Assert.assertNotEquals(names, dataList);
        dataList.clear();
    }
    @Test(description = "User can create a smart playlist with one rule and verify related songs appear")
    public void createSmartPlaylist() {
        setupKoel();
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
    @Test(description = "Verify existing smart playlist name can be edited")
    public void editListName() throws SQLException, ClassNotFoundException {
        String newName = generatePlaylistName(6);
        setupKoel();
        String oldPlId = DbTestUtil.getSmartPlInfo("p.id", System.getProperty("koelUser"), homePage.getFirstSmartPlName());
        TestListener.logInfoDetails("Smart playlist name before: " + homePage.getFirstSmartPlName());
        dataMap.put("oldId", oldPlId);
        homePage.cmEditFirstSmartPl()
                .editSmartPlName(newName)
                .clickSaveSmartList();
        dataMap.put("editedName", newName);
        TestListener.logInfoDetails("Smart playlist name after: " + homePage.getFirstSmartPlName());
        TestListener.logAssertionDetails("User can edit smart playlist name: " + homePage.getFirstSmartPlName().contains(newName));
        Assert.assertTrue(homePage.getFirstSmartPlName().contains(newName), "Unable to edit smart playlist name");
    }
    @Test(description =  "Verify the edited smart playlist is updated correctly in the database", dependsOnMethods = {"editListName"})
    public void checkDbForEditedPl() throws SQLException, ClassNotFoundException {
        String editedPlId = DbTestUtil.getSmartPlInfo("p.id", System.getProperty("koelUser"), dataMap.get("editedName"));
        String oldPlId = dataMap.get("oldId");
        TestListener.logRsDetails("Smart playlist id before editing name: " + oldPlId);
        TestListener.logRsDetails("Smart playlist id after editing name: " + editedPlId);
        TestListener.logAssertionDetails("Smart playlist id's match before/after name being updated: " + oldPlId.equals(editedPlId));
        Assert.assertEquals(oldPlId, editedPlId);
        dataMap.clear();
    }
    @Test(enabled = false, description = "Execute SQL query to verify new user info is stored correctly or updated in the Koel database", groups = {"Account Creation"}, priority=3)
    @Parameters({"koelNewUser", "password"})
    public void queryDbForNewUser(String koelNewUser, String password) throws SQLException {
        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.getUserInfo(koelNewUser);
        if (rs.next()) {
            String ep = rs.getString("password");
            String updated = rs.getString("updated_at");
            String email = rs.getString("email");
            TestListener.logRsDetails(
                    "Results: " +"\n" +"<br>"+
                            "encrypted password: " + ep +"\n" +"<br>"+
                            "updated_at: " + updated +"\n" +"<br>"+
                            "user: " + email +"\n" +"<br>"
            );
            rsMap.put("existingUser", email);   //store the account email to use for the next test
            TestListener.logAssertionDetails("New user data has been saved correctly in the database: " + email.equals(koelNewUser));
            Assert.assertNotSame(ep, password);
            Assert.assertEquals(email, koelNewUser);
        }
    }
    @Test(enabled = false, description = "Get existing user from database, attempt to register with that account", groups = {"Account Creation"}, priority=4)
    public void tryRegisteringExistingUser() {
        setupKoel();
        String existingUser = rsMap.get("existingUser").toString(); //this value comes from the previous db query
        TestListener.logInfoDetails("Existing Account : " + existingUser);
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.provideEmail(existingUser)
                .clickSubmit();
        TestListener.logRsDetails("Confirmation Message: " + registrationPage.confirmationMsgText());
        TestListener.logAssertionDetails("Confirmation Message is displayed: " + registrationPage.getConfirmationMsg());
        Assert.assertTrue(registrationPage.getConfirmationMsg());
    }
    @Test(enabled = false, description = "Execute SQL query to find an existing user", priority=0)
    @Parameters({"koelExistingUser"})
    public void queryDbForExistingUser(String koelExistingUser) throws SQLException, ClassNotFoundException {

        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.getUserInfo(koelExistingUser);
        if (rs.next()) {
            String email = rs.getString("email");
            TestListener.logRsDetails(
                    "Results: " + "\n" + "<br>" +
                            "user: " + email + "\n" + "<br>"
            );
            dataMap.put("existingUser", email);   //store the account email to use for the next test
            TestListener.logRsDetails("Existing user: " + email);
            TestListener.logAssertionDetails("User already exists in database: " + email.equals(koelExistingUser));
            Assert.assertEquals(email, koelExistingUser);
        }
    }
    @Test(description = "Execute SQL query to verify password is encrypted and has been updated in the Koel database", groups = {"Update password"})
    public void queryDbPwd() throws SQLException {
        KoelDbActions koelDbActions = new KoelDbActions();
        TestListener.logInfoDetails("Db connection: " + KoelDb.getDbConnection().getMetaData().getURL());
        rs = koelDbActions.getPwdInfo(System.getProperty("koelUser"));
        if (rs.next()) {
            String ep = rs.getString("password");
            String updated = rs.getString("updated_at");
            TestListener.logRsDetails(
                    "Results: " +"\n" +"<br>"+
                            "encrypted password: " + ep +"\n" +"<br>"+
                            "updated_at: " + updated +"\n" +"<br>"+
                            "user: " + System.getProperty("koelUser")
            );
            TestListener.logAssertionDetails("Assertion: " + updated + " contains " + TestUtil.getDate());
            TestListener.logAssertionDetails("Assertion: " + ep + " notSame " + System.getProperty("updatedPassword"));
            Assert.assertNotSame(ep, System.getProperty("updatedPassword"));
//            Assert.assertTrue(updated.contains(TestUtil.getDate()));
        }
    }
}


