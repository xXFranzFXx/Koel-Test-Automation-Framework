package models.song;

import models.artist.Artist;

public class SongInfo {
    private long id;
    private int album_id;
    private int artist_id;
    private String title;
    private double length;
    private int track;
    private int disc;
    private String created_at;
    private Artist[] artist;


    public SongInfo(Artist[] artist) {
        this.artist = artist;
    }
}
