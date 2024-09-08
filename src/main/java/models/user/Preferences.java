package models.user;

public class Preferences {
    private String lastfm_session_key;
    public Preferences(String lastfm_session_key) {
        this.lastfm_session_key = lastfm_session_key;
    }
    public Preferences() {
        this(null);
    }
    public String getLastfm_session_key() {
        return lastfm_session_key;
    }
    public void setLastfm_session_key(String lastfm_session_key) {
        this.lastfm_session_key = lastfm_session_key;
    }
}

