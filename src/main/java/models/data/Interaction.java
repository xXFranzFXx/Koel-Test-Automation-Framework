package models.data;

public class Interaction {
    private String song_id;
    private boolean liked;
    private int play_count;

    public Interaction(String song_id, boolean liked, int play_count) {
        this.song_id = song_id;
        this.liked = liked;
        this.play_count = play_count;
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
}
