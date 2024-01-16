package db;

import io.github.cdimascio.dotenv.Dotenv;
import org.testng.Assert;
import org.testng.annotations.*;
import util.ExcelFileUtil;
import util.TestDataHandler;
import util.TestUtil;
import util.listeners.TestListener;
import util.extentReports.ExtentManager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.ExcelFileUtil;

public class KoelDbTests extends KoelDbActions {
    ResultSet rs;
    TestDataHandler testData =new TestDataHandler();
    Map<String,Object> dataMap = new HashMap<>();
    //Verify the data saved in previous test is correct
    public boolean verifyData(String key1, String key2) {
        Map<String, Object> testDataInMap = testData.getTestDataInMap();
        String dataKey1 = testDataInMap.get(key1).toString();
        String dataKey2 = testDataInMap.get(key2).toString();
        System.out.println("dataKey1 " + dataKey1);
        System.out.println("dataKey2 " + dataKey2);
        return dataKey1.equals(dataKey2);
    }
    public void addDataFromTest(String key, String value) {
        dataMap.put(key, value);
        testData.setTestDataInMap(dataMap);
    }
    @BeforeClass
    public static void setEnv() {
        Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }
    @BeforeMethod
    public void setupDb() throws SQLException, ClassNotFoundException {
        initializeDb();
    }
    @AfterMethod
    public void closeDbConnection() throws SQLException {
        closeDatabaseConnection();
        dataMap.clear();
        testData.setTestDataInMap(dataMap);
    }
    @Test(description = "get artist info")
    @Parameters({"artist"})
    public void queryArtist(String artist) throws SQLException {
        ExcelFileUtil.generateExcel(TestUtil.processResultSet(rs), "test.xlsx", "artistQuery");
        rs = artistQuery(artist);
        if (rs.next()) {

            TestListener.logPassDetails("Results: " +"\n" +"<br>"+
                    "id: " + rs.getString("id") +"\n" +"<br>"+
                    "name: " + rs.getString("name") +"\n" +"<br>"+
                    "query: " + artist
            );
            addDataFromTest("id", rs.getString("id"));
            addDataFromTest("name", rs.getString("name"));
            addDataFromTest("queryName", artist);
            testData.setTestDataInMap(dataMap);
            Assert.assertEquals(rs.getString("name"), artist);
        }
        Assert.assertFalse(false);
    }
    public void logResultSetDetails(Map<String, Object> results) {

    }
    @Test(dependsOnMethods = {"queryArtist"})
    public void verifyArtistQueryResults(){
        Assert.assertTrue(verifyData("name", "queryName"));
    }
    @Test(description = "get songs by an artist")
    @Parameters({"artist"})
    public void querySongByArtist(String artist) throws SQLException {
        rs = songByArtistJoinStmt(artist);
        ExcelFileUtil.generateExcel(TestUtil.processResultSet(rs), "test.xlsx", "songsByArtist");
        if(rs.next()){
            int artistID = rs.getInt("artist_id");
            int id = rs.getInt("a.id");
            String songArtist = rs.getString("a.name");
            String songTitle = rs.getString("title");

            Assert.assertEquals(artistID, id);
            Assert.assertEquals(songArtist, artist);
        }
        Assert.assertFalse(false);
    }
    @Test(description = "get the total amount of songs in the database")
    public void getSongTotal() throws SQLException {
        rs = totalSongCount();
        if(rs.next()) {
            addDataFromTest("count", rs.getString("count"));
            testData.setTestDataInMap(dataMap);
            int count = rs.getInt("count");
            Assert.assertEquals(count, 66);
        }
        Assert.assertFalse(false);
    }

    @Test(description = "get a user's playlists and write the data from the result set to excel file")
    @Parameters({"koelUser"})
    public void getKoelUserPlaylists(String koelUser) throws SQLException, IOException {
        rs = getUserPlaylst(koelUser);
        System.out.println(rs.getMetaData());
        ExcelFileUtil.generateExcel(TestUtil.processResultSet(rs), "test.xlsx", "userPlaylists");
        if(rs.next()) {
            String p_uid = rs.getString("p.user_id");
            String u_id = rs.getString("u.id");
            String email = rs.getString("email");
            addDataFromTest("playlistSearchUserEmail", email);
            addDataFromTest("playlistUserId", p_uid);
            addDataFromTest("searchedUserId", u_id);
            testData.setTestDataInMap(dataMap);
            Assert.assertEquals(p_uid, u_id);
        }
        Assert.assertFalse(false);
    }
}
