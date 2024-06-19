package testcases;

import db.KoelDbActions;
import io.github.cdimascio.dotenv.Dotenv;
import org.testng.Assert;
import org.testng.annotations.*;
import util.dbUtils.DbTestDataHandler;
import util.listeners.TestListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class KoelDbTests extends KoelDbActions {
    DbTestDataHandler testData = new DbTestDataHandler();

    //Verify the data saved in previous test is correct
    private boolean verifyData(String key1, String key2) {
        Map<String, ResultSet> testDataInMap = testData.getTestDataInMap();
        String dataKey1 = testDataInMap.get(key1).toString();
        String dataKey2 = testDataInMap.get(key2).toString();
        System.out.println("dataKey1 " + dataKey1);
        System.out.println("dataKey2 " + dataKey2);
        return dataKey1.equals(dataKey2);
    }
    private void addDataFromTest(String key, ResultSet rs) {
       testData.addTestData(key, rs);
    }

    @BeforeClass
    public static void setEnv() throws SQLException, ClassNotFoundException {
        Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        initializeDb();
    }
    @AfterClass
    public void closeDbConnection() throws SQLException {
        testData.clearData();
        closeDatabaseConnection();
    }
    @Test(description = "get artist info")
    @Parameters({"artist"})
    public void queryArtist(String artist) throws SQLException {
        try(ResultSet rs = artistQuery(artist)) {
            addDataFromTest("artistQuery", rs);
            if (rs.next()) {
                TestListener.logPassDetails("Results: " + "\n" + "<br>" +
                        "id: " + rs.getString("id") + "\n" + "<br>" +
                        "name: " + rs.getString("name") + "\n" + "<br>" +
                        "query: " + artist
                );
                Assert.assertEquals(rs.getString("name"), artist);
            }
        }
    }
    @Test(description = "get all artists in ascending order")
    @Parameters({"artist"})
    public void queryArtists(String artist) throws SQLException {
        List<String> names = new ArrayList<>();
        try (ResultSet rs = checkArtistsInDb()) {
            addDataFromTest("allArtistsQuery", rs);
            while (rs.next()) {
                TestListener.logRsDetails("Results: " + "\n" + "<br>" +
                        "name: " + rs.getString("a.name") + "\n" + "<br>");
                names.add(rs.getString("a.name"));
            }
            Assert.assertTrue(names.contains(artist));
        }
    }

    @Test(description = "get songs by an artist")
    @Parameters({"artist"})
    public void querySongByArtist(String artist) throws SQLException {
        try (ResultSet rs = songByArtistJoinStmt(artist)) {
            addDataFromTest("songByArtist", rs);
            if (rs.next()) {
                int artistID = rs.getInt("s.artist_id");
                int id = rs.getInt("a.id");
                String songArtist = rs.getString("a.name");
                String songTitle = rs.getString("s.title");
                TestListener.logRsDetails("songArtist: " + songArtist);
                TestListener.logRsDetails("artistID: " + artistID);
                Assert.assertEquals(artistID, id);
                Assert.assertEquals(songArtist, artist);
            }
        }
    }
    @Test(description = "get the total amount of songs in the database")
    public void getSongTotal() throws SQLException {
        try (ResultSet rs = totalSongCount()) {
            addDataFromTest("totalSongCount", rs);
            if (rs.next()) {
                int count = rs.getInt("count");
                Assert.assertEquals(count, 66);
            }
        }
    }

    @Test(description = "get a user's playlists and write the data from the result set to excel file")
    @Parameters({"koelUser"})
    public void getKoelUserPlaylists(String koelUser) throws SQLException {
        try (ResultSet rs = getUserPlaylst(koelUser)) {
            addDataFromTest("getKoelUserPlaylists", rs);
            if (rs.next()) {
                String p_uid = rs.getString("p.user_id");
                String u_id = rs.getString("u.id");
                String email = rs.getString("email");
                Assert.assertEquals(p_uid, u_id);
            }
        }
    }
}
