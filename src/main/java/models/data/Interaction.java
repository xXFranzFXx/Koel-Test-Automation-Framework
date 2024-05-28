package models.data;

import models.user.User;

public class Interaction {
    private Song song;
   private User user;
   private String song_id;
   private boolean liked;
   private int play_count;
    public Interaction(String song_id, boolean liked, int play_count, Song song, User user) {
        this.song_id = song_id;
        this.liked = liked;
        this.play_count = play_count;
        this.song = song;
        this.user = user;
    }
    public Interaction() {

    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
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
