package util.dbUtils;

import db.KoelDbActions;
import util.listeners.TestListener;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbTestUtil {
    public static boolean checkDatabaseForPlaylist(String koelUser, String playlistName, ResultSet rs) throws SQLException {
        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.checkNewPlaylist(koelUser, playlistName);
        if(rs.next()) {
            String playlist = rs.getString("p.name");
            TestListener.logInfoDetails("Playlist in database: " + playlist);
            TestListener.logAssertionDetails("Created playlist exists in database: " + playlistName.equalsIgnoreCase(playlist));
            return playlistName.equalsIgnoreCase(playlist);
        }
        return false;
    }
    public static boolean checkDatabaseForSongInPlaylist(String koelUser, String song, ResultSet rs) throws SQLException {
        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.checkSongsInPlaylist(koelUser);
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
    public static boolean duplicateCondition (int duplicates) {
        return duplicates >= 2;
    }
    public static int countDuplicateNames(String koelUser, String playlistName, ResultSet rs) throws SQLException{
        KoelDbActions koelDbActions = new KoelDbActions();
        int duplicates = 0;
        rs = koelDbActions.checkDuplicatePlaylistNames(koelUser, playlistName);
        if (rs.next()) {
            duplicates = rs.getInt("count");
            TestListener.logInfoDetails("Total playlists with same name: " + duplicates);
            TestListener.logAssertionDetails("User can create playlists with duplicate names: " + duplicateCondition(duplicates));
        }
        return duplicates;
    }
    public static String getSmartPlInfo(String property, String user, String smartPl, ResultSet rs) throws SQLException, ClassNotFoundException {
        KoelDbActions koelDbActions = new KoelDbActions();
        rs = koelDbActions.checkSmartPl(user, smartPl);
        String result = "";
        if(rs.next()) {
            result = rs.getString(property);
            TestListener.logInfoDetails("Smart playlist being checked in db: " + smartPl);
            TestListener.logInfoDetails("Retrieving smart playlist property from database: " + property);
            TestListener.logRsDetails("SQL query result: " + result);
        }
        return result;
    }
    public List<String> getSongTitles(List<String> songIds) throws SQLException {
        KoelDbActions koelDbActions = new KoelDbActions();
        List<String> songTitles =  new ArrayList<>();
        ResultSet rs = koelDbActions.checkSongsFromList(songIds, "id");
        while(rs.next()){
            songTitles.add(rs.getString("title"));
        }
        return songTitles;
    }
}
