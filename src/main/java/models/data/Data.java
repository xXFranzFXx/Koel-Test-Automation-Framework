package models.data;

import models.album.Album;
import models.artist.Artist;
import models.playlist.Playlist;
import models.user.User;

public class Data {
    private Album[] albums;
    private Artist[] artists;
    private Song[] songs;
    private String[] settings;
    private Playlist[] playlists;
    private Interaction[] interactions;
    private String[] recentlyPlayed;
    private User[] users;
    private User currentUser;
    private boolean useLastfm;
    private boolean useYouTube;
    private boolean useiTunes;
    private boolean allowDownload;
    private boolean supportsTranscoding;
    private String cdnUrl;
    private String currentVersion;
    private String latestVersion;


    public Data(Album[] albums, Song[] songs, String[] settings, Playlist[] playlists, Interaction[] interactions, String[] recentlyPlayed, User[] users, User currentUser, boolean useLastfm, boolean useYouTube, boolean useiTunes, boolean allowDownload, boolean supportsTranscoding, String cdnUrl, String currentVersion, String latestVersion) {
        this.albums = albums;
        this.songs = songs;
        this.settings = settings;
        this.playlists = playlists;
        this.interactions = interactions;
        this.recentlyPlayed = recentlyPlayed;
        this.users = users;
        this.currentUser = currentUser;
        this.useLastfm = useLastfm;
        this.useYouTube = useYouTube;
        this.useiTunes = useiTunes;
        this.allowDownload = allowDownload;
        this.supportsTranscoding = supportsTranscoding;
        this.cdnUrl = cdnUrl;
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
    }
    public Data() {

    }
    public Interaction[] getInteractions() {
        return interactions;
    }

    public Song[] getSongs() {
        return songs;
    }

    public boolean isUseLastfm() {
        return useLastfm;
    }

    public void setUseLastfm(boolean useLastfm) {
        this.useLastfm = useLastfm;
    }

    public void setInteractions(Interaction[] interactions) {
        this.interactions = interactions;
    }

    public Album[] getAlbums() {
        return albums;
    }

    public void setAlbums(Album[] albums) {
        this.albums = albums;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public void setArtists(Artist[] artists) {
        this.artists = artists;
    }

    public void setSongs(Song[] songs) {
        this.songs = songs;
    }
    public String[] getSettings() {
        return settings;
    }

    public void setSettings(String[] settings) {
        this.settings = settings;
    }

    public Playlist[] getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Playlist[] playlists) {
        this.playlists = playlists;
    }

    public String[] getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(String[] recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isUseYouTube() {
        return useYouTube;
    }

    public void setUseYouTube(boolean useYouTube) {
        this.useYouTube = useYouTube;
    }

    public boolean isUseiTunes() {
        return useiTunes;
    }

    public void setUseiTunes(boolean useiTunes) {
        this.useiTunes = useiTunes;
    }

    public boolean isAllowDownload() {
        return allowDownload;
    }

    public void setAllowDownload(boolean allowDownload) {
        this.allowDownload = allowDownload;
    }

    public boolean isSupportsTranscoding() {
        return supportsTranscoding;
    }

    public void setSupportsTranscoding(boolean supportsTranscoding) {
        this.supportsTranscoding = supportsTranscoding;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

}
