package models.song;


import models.album.Album;

public class Song {
    private long song_id;
    private boolean liked;
    private int play_count;
    private SongInfo[] songInfo;
    private Album[] albums;



    public Song(boolean liked, SongInfo[] songInfo, int play_count, long song_id, Album[] albums) {
        this.liked = liked;
        this.songInfo = songInfo;
        this.play_count = play_count;
        this.song_id = song_id;
        this.albums = albums;
    }

    public Song() {

    }
    public long getSong_Id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }
    public SongInfo[] getSongInfo() {
        return songInfo;
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
