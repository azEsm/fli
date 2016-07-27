package ml.fli.models;

import java.util.Set;

public class User {
    private int id;

    private String first_name;

    private String last_name;

    private String sex;

    private String city;

    private String bdate;

    private String photo_400_orig;

    private Set<String> user_audio;

    private Set<String> user_group;

    public Set<String> getUser_audio() {
        return user_audio;
    }

    public void setUser_audio(Set<String> user_audio) {
        this.user_audio = user_audio;
    }

    public Set<String> getUser_group() {
        return user_group;
    }

    public void setUser_group(Set<String> uer_group) {
        this.user_group = uer_group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String home_town) {
        this.city = city;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getPhoto_400_orig() {
        return photo_400_orig;
    }

    public void setPhoto_400_orig(String photo_400_orig) {
        this.photo_400_orig = photo_400_orig;
    }

    /*    public String toString() {
        return "User [name = " + first_name + " " + last_name + "; sex =" + sex
            + "; home_town = " + city + "]"
            + "bdate = " + bdate;
    }*/

    public User() {

    }

    public User(int id, String first_name, String last_name, String sex, String bdate, String city) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.sex = sex;
        this.bdate = bdate;
        this.city = city;
    }

}
