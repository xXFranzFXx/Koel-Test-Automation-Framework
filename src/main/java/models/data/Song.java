package models.data;

public class Song {
    private String id;
    private int album_id;
    private int artist_id;
    private String title;
    private double length;
    private int track;
    private int disc;
    private String created_at;
    public Song(String id, int album_id, int artist_id, String title, double length, int track, int disc, String created_at) {
        this.id = id;
        this.album_id = album_id;
        this.artist_id = artist_id;
        this.title = title;
        this.length = length;
        this.track = track;
        this.disc = disc;
        this.created_at = created_at;
    }
    public Song() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public int getDisc() {
        return disc;
    }

    public void setDisc(int disc) {
        this.disc = disc;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
