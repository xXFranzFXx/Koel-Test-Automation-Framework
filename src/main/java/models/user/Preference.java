package models.user;

public class Preference {
    private String lastfm_session_key;
    public Preference(String lastfm_session_key) {
        this.lastfm_session_key = lastfm_session_key;
    }
    public String getLastfm_session_key() {
        return lastfm_session_key;
    }
    public void setLastfm_session_key(String lastfm_session_key) {
        this.lastfm_session_key = lastfm_session_key;
    }
}
