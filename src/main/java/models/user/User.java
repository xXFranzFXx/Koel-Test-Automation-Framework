package models.user;

public class User {
    private int id;
    private String name;
    private String email;
    private boolean is_admin;
    private Preference[] preferences;
    public User(String name, String email, boolean is_admin, Preference[] preferences) {
        this.name = name;
        this.email = email;
        this.is_admin = is_admin;
        this.preferences = preferences;
    }
    public int getId() {
         return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean getIs_admin() {
        return is_admin;
    }
    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
    public Preference[] getPreferences() {
        return preferences;
    }
    public void setPreferences(Preference[] preferences) {
        this.preferences = preferences;
    }
}
