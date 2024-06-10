package models.information;

public class Song {
    private String lyrics;
    private String album_info;
    private String artist_info;
    private String youtube;
    private String message;

    public Song(String lyrics, String album_info, String artist_info, String youtube, String message) {
        this.lyrics = lyrics;
        this.album_info = album_info;
        this.artist_info = artist_info;
        this.youtube = youtube;
        this.message = message;
    }
    public Song () {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(String album_info) {
        this.album_info = album_info;
    }

    public String getArtist_info() {
        return artist_info;
    }

    public void setArtist_info(String artist_info) {
        this.artist_info = artist_info;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

}
