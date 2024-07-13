package models.album;
public class Album {
   private int id;
   private int artist_id;
   private String  name;
   private String cover;
   private String created_at;
   private boolean is_compilation;
   public Album(int id, int artist_id, String name, String cover, String created_at, boolean is_compilation) {
       this.id = id;
       this.artist_id = artist_id;
       this.name = name;
       this.cover = cover;
       this.created_at = created_at;
       this.is_compilation = is_compilation;
   }
   public Album () {}
   public int getId() {
       return id;
   }
   public void setId(int id) {
       this.id = id;
   }
    public int getArtist_id() {
        return artist_id;
    }
    public void setArtist_id(int artist_id) {
       this.artist_id = artist_id;
    }
   public String getName() {
       return name;
   }
   public void setName(String name) {
       this.name = name;
   }
   public String getCover() {
       return cover;
   }
   public void setCover(String cover) {
       this.cover = cover;
   }
   public String getCreated_at() {
       return created_at;
   }
   public void setCreated_at(String createdAt) {
       this.created_at = createdAt;
   }
   public boolean getIs_compilation() {
       return is_compilation;
   }
   public void setIs_compilation(boolean is_compilation) {
       this.is_compilation = is_compilation;
   }
}
