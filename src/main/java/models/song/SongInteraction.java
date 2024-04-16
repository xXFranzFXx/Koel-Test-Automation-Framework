package models.song;

import models.user.User;

public class SongInteraction {
    private Song song;
    private User user;

    public SongInteraction(Song song, User user) {
        this.song = song;
        this.user = user;
    }
    public SongInteraction() {

    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
