package models.song;


import models.album.Album;

public class Song {
    private String song_id;
    private boolean liked;
    private int play_count;
    private SongInfo[] song;
    private Album[] albums;



    public Song(boolean liked, SongInfo[] song, int play_count, String song_id, Album[] albums) {
        this.liked = liked;
        this.song = song;
        this.play_count = play_count;
        this.song_id = song_id;
        this.albums = albums;
    }

    public Song() {

    }
    public String getSong_Id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }
    public SongInfo[] getSongInfo() {
        return song;
    }
    public Album[] getAlbums() {
        return albums;
    }

    public int getPlayCount() {
        return play_count;
    }
    public boolean isLiked() {
        return liked;
    }
}
