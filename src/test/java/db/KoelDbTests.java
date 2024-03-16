package db;

import io.github.cdimascio.dotenv.Dotenv;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import util.ExcelFileUtil;
import util.TestDataHandler;
import util.TestUtil;
import util.listeners.TestListener;
import util.extentReports.ExtentManager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.ExcelFileUtil;

public class KoelDbTests extends KoelDbActions {
    ResultSet rs;
    TestDataHandler testData =new TestDataHandler();
    public static Map<String,ResultSet> dataMap = new HashMap<>();
    //Verify the data saved in previous test is correct
//    public boolean verifyData(String key1, String key2) {
//        Map<String, ResultSet> testDataInMap = testData.getTestDataInMap();
//        String dataKey1 = testDataInMap.get(key1).toString();
//        String dataKey2 = testDataInMap.get(key2).toString();
//        System.out.println("dataKey1 " + dataKey1);
//        System.out.println("dataKey2 " + dataKey2);
//        return dataKey1.equals(dataKey2);
//    }
    public void addDataFromTest(String key, ResultSet rs) {
        dataMap.put(key, rs);
        testData.setTestDataInMap(dataMap);

    }
    private boolean checkDatabaseForPlaylist(String koelUser, String playlistName) throws SQLException, ClassNotFoundException {
        rs = checkNewPlaylist(koelUser, playlistName);
        if(rs.next()) {
            String playlist = rs.getString("p.name");
            TestListener.logInfoDetails("Playlist in database: " + playlist);
            TestListener.logAssertionDetails("Created playlist exists in database: " + playlistName.equalsIgnoreCase(playlist));
            return playlistName.equalsIgnoreCase(playlist);
        }
        return false;
    }
    private boolean checkDatabaseForSongInPlaylist(String koelUser, String song) throws SQLException, ClassNotFoundException {

        rs = checkSongsInPlaylist(koelUser);
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        final int columnCount = resultSetMetaData.getColumnCount();
        boolean found = false;
        TestListener.logInfoDetails("Searching for song containing the word '"+song+"' ");
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String dbSong = rs.getString(i).toLowerCase();
                found = dbSong.contains(song);
                TestListener.logInfoDetails("Playlist song found: " + dbSong);
                TestListener.logAssertionDetails("Song added to playlist matches playlist song found in database: " + dbSong.contains(song));
                if (found) break;
            }
        }
        return found;
    }
    public boolean duplicateCondition (int duplicates) {
        return duplicates >= 2;
    }
    private int countDuplicateNames(String koelUser, String playlistName) throws SQLException, ClassNotFoundException {
        int duplicates = 0;
        rs = checkDuplicatePlaylistNames(koelUser, playlistName);
        if (rs.next()) {
            duplicates = rs.getInt("count");
            TestListener.logInfoDetails("Total playlists with same name: " + duplicates);
            TestListener.logAssertionDetails("User can create playlists with duplicate names: " + duplicateCondition(duplicates));
        }
        return duplicates;
    }
    @BeforeClass
    public static void setEnv() {
        Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }
    @BeforeMethod
    public void setupDb() throws SQLException, ClassNotFoundException {
        initializeDb();
        dataMap.clear();
    }
    @AfterClass
    public void closeDbConnection() throws SQLException, IOException {
        ExcelFileUtil.writeWithoutDuplicates("dbResults.xlsx");
        closeDatabaseConnection();
    }
    @Test(description = "get artist info")
    @Parameters({"artist"})
    public void queryArtist(String artist) throws SQLException, IOException {
        rs = artistQuery(artist);
        addDataFromTest("artistQuery", rs);
        ExcelFileUtil.generateExcel(dataMap, "dbResults.xlsx");

        if (rs.next()) {

            TestListener.logPassDetails("Results: " +"\n" +"<br>"+
                    "id: " + rs.getString("id") +"\n" +"<br>"+
                    "name: " + rs.getString("name") +"\n" +"<br>"+
                    "query: " + artist
            );
            Assert.assertEquals(rs.getString("name"), artist);
        }

        Assert.assertFalse(false);
    }

    @Test(description = "get songs by an artist")
    @Parameters({"artist"})
    public void querySongByArtist(String artist) throws SQLException, IOException {
        rs = songByArtistJoinStmt(artist);
        addDataFromTest("songByArtist", rs);
        ExcelFileUtil.generateExcel(dataMap, "dbResults.xlsx");

        if(rs.next()){
            int artistID = rs.getInt("s.artist_id");
            int id = rs.getInt("a.id");
            String songArtist = rs.getString("a.name");
            String songTitle = rs.getString("s.title");
            TestListener.logRsDetails("songArtist: " + songArtist);
            TestListener.logRsDetails("artistID: " + artistID);
            Assert.assertEquals(artistID, id);
            Assert.assertEquals(songArtist, artist);
        }
        Assert.assertFalse(false);
    }
    @Test(description = "get the total amount of songs in the database")
    public void getSongTotal() throws SQLException, IOException {
        rs = totalSongCount();
        addDataFromTest("totalSongCount", rs);
        ExcelFileUtil.generateExcel(dataMap, "dbResults.xlsx");

        if(rs.next()) {
            int count = rs.getInt("count");
            Assert.assertEquals(count, 66);
        }
        Assert.assertFalse(false);
    }

    @Test(description = "get a user's playlists and write the data from the result set to excel file")
    @Parameters({"koelUser"})
    public void getKoelUserPlaylists(String koelUser) throws SQLException, IOException {
        rs = getUserPlaylst(koelUser);
        addDataFromTest("getKoelUserPlaylists", rs);
        ExcelFileUtil.generateExcel(dataMap, "dbResults.xlsx");

        if(rs.next()) {
            String p_uid = rs.getString("p.user_id");
            String u_id = rs.getString("u.id");
            String email = rs.getString("email");
            Assert.assertEquals(p_uid, u_id);
        }
        Assert.assertFalse(false);
    }

}
