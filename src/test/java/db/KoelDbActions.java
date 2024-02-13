package db;

import org.mariadb.jdbc.Connection;
import util.listeners.TestListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class KoelDbActions extends KoelDb{
    private static Connection db ;
    private static PreparedStatement st;
    private static ResultSet rs;
    private String getSmartPLaylist = """
            SELECT * FROM dbkoel.playlists p JOIN dbkoel.users u ON p.user_id = u.id WHERE u.email= ? AND p.name= ?
            """;
    private String getArtistsInDb = """
            SELECT a.name as name FROM dbkoel.artists a  ORDER BY a.name ASC
            """;

    private final String getUserPwdInfo = """
            SELECT password, updated_at FROM dbkoel.users u WHERE u.email = ?
            """;
    private final String getNewUser = """
            SELECT * FROM dbkoel.users u WHERE u.email = ?
            """;
    private String getUserPlaylists = """
            SELECT * FROM dbkoel.users u JOIN dbkoel.playlists p ON u.id = p.user_id WHERE u.email = ?
            """;
    private String getTotalSongCount = """
            SELECT COUNT(*) as count FROM dbkoel.songs
            """;
    private String artistNameQuery = """
        SELECT * FROM artists WHERE name = ?
        """;
    private String getSongByArtist = """
            SELECT * FROM dbkoel.songs s
            LEFT JOIN dbkoel.artists a
            ON a.id = s.artist_id WHERE a.name = ?
            """;
    private String songSpecificLength = """
            SELECT * FROM dbkoel.songs WHERE LENGTH BETWEEN ? AND ?""";
    private String usersThatHavePlaylsts = """
            SELECT * FROM dbkoel.users u RIGHT JOIN dbkoel.playlists p ON u.id = p.user_id""";
    private final String getTotalDuration = """
             SELECT SUM(length) as duration FROM dbkoel.songs
             """;
    private final String getDuration = """
            SELECT SUM(duration.length/60/60) FROM (SELECT * FROM dbkoel.songs  LIMIT = ?) as duration
            """;
    private String checkNewPlaylistName = """
            SELECT p.name FROM dbkoel.users u JOIN dbkoel.playlists p ON u.id = p.user_id WHERE u.email = ? AND p.name = ?
            """;
    private String getSongsInPlaylist = """
            SELECT s.title FROM dbkoel.songs s JOIN dbkoel.playlist_song ps ON s.id = ps.song_id JOIN dbkoel.playlists p ON ps.playlist_id  = p.id  JOIN dbkoel.users u ON p.user_id = u.id WHERE u.email = ?
            """;
    private String getDuplicatePlaylistNames = """
            SELECT COUNT(*) as count FROM dbkoel.playlists p JOIN dbkoel.users u ON p.user_id = u.id WHERE u.email = ? AND p.name = ?
            """;
    private String dbArtistsInList = """
           SELECT COUNT(*) as count FROM dbkoel.artists a
           """;
    private static String createQueryList(int length, String sql, String property) {
        String query = sql + "WHERE " + property + " in (";
        StringBuilder queryBuilder = new StringBuilder(query);
        for (int i = 0; i < length; i++) {
            queryBuilder.append(" ?");
            if (i != length - 1)
                queryBuilder.append(",");
        }
        queryBuilder.append(")");
        return queryBuilder.toString();
    }
    private ResultSet processDynamicQuery(List<String> list, String sql, String property) throws SQLException {
        String queryStr = createQueryList(list.size(), sql, property);
        String[] stringArr = list.toArray(new String[0]);
        return query(queryStr, stringArr);
    }
    private ResultSet simpleQuery(String sql) throws SQLException {
        db = getDbConnection();
        st=db.prepareStatement(sql);
        rs=st.executeQuery();
        TestListener.logInfoDetails("SQL statement: " + sql);
        return rs;
    }
    public ResultSet query(String sql, String ...args) throws SQLException {
        TestListener.logInfoDetails("SQL statement: " + sql);
        db = getDbConnection();
        st = db.prepareStatement(sql);
        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].matches("-?(0|[1-9]\\d*)")) {
                    st.setInt(i+1 , Integer.parseInt(args[i]));
                }
                st.setString(i+1, args[i]);
            }
        } else {
            st.setString(1, args[0]);
        }
        rs = st.executeQuery();
        return rs;
    }
    public ResultSet artistQuery(String artist) throws SQLException {
        TestListener.logInfoDetails("Artist " + artist);
        String[] str = new String[]{artist};
        return query(artistNameQuery, str);
    }
    public ResultSet songByArtistJoinStmt(String artist) throws SQLException {
        String[] str = new String[]{artist};
        return query(getSongByArtist, str);
    }
    public ResultSet totalSongCount() throws SQLException {
        return simpleQuery(getTotalSongCount);
    }
    public ResultSet getUserPlaylst(String user) throws SQLException {
        TestListener.logInfoDetails("User " + user);
        String[] str = new String[]{user};
        return query(getUserPlaylists, str);
    }
    public ResultSet getPwdInfo(String user) throws SQLException {
        TestListener.logInfoDetails("String user: " + user);
        String[] str = new String[]{user};
        return query(getUserPwdInfo, str);
    }
    public ResultSet getUserInfo(String user) throws SQLException {
        TestListener.logInfoDetails("String user: " + user);
        String[] str = new String[]{user};
        return query(getNewUser, str);
    }
    public ResultSet totalDuration() throws SQLException {
        return simpleQuery(getTotalDuration);
    }
    public ResultSet getSpecificDuration(String songTotal) throws SQLException {
        TestListener.logInfoDetails("String songTotal: " + songTotal);
        String[] str = new String[]{songTotal};
        return query(getDuration, str);
    }
    public ResultSet checkNewPlaylist(String user, String playlist) throws SQLException {
        TestListener.logInfoDetails("User: " + user);
        TestListener.logInfoDetails("New playlist name: " + playlist);
        String[] str = new String[]{user, playlist};
        return query(checkNewPlaylistName, str);
    }
    public ResultSet checkSongsInPlaylist(String user) throws SQLException {
        String[] str = new String[]{user};
        return query(getSongsInPlaylist, str);
    }
    public ResultSet checkDuplicatePlaylistNames(String user, String playlist) throws SQLException {
        String[] str = new String[]{user, playlist};
        return query(getDuplicatePlaylistNames, str);
    }
    public ResultSet checkSmartPl(String user, String smartPl) throws SQLException {
        String[] str = new String[]{user, smartPl};
        return query(getSmartPLaylist, str);
    }
    public ResultSet checkArtistsInDb() throws SQLException {
        return simpleQuery(getArtistsInDb);
    }
    public ResultSet checkArtistsListFromApp(List<String> artists, String property) throws SQLException {
        return processDynamicQuery(artists, dbArtistsInList, property);
    }
}