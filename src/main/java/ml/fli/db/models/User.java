package ml.fli.db.models;

//import javax.persistence.CollectionTable;
//import javax.persistence.ElementCollection;
//import javax.persistence.Entity;
//import javax.persistence.Id;
import java.util.Set;

//@Entity
public class User {
//    @Id
    private long id;

    private String first_name;

    private String last_name;

    private String sex;

    private String city;

    private String bdate;

    private String photo_400_orig;

//    @ElementCollection
//    @CollectionTable(name = "audios")
    private Set<String> audio;

//    @ElementCollection
//    @CollectionTable(name = "groups")
    private Set<String> groups;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setCity(String city) {
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

    public Set<String> getAudio() {
        return audio;
    }

    public void setAudio(Set<String> audio) {
        this.audio = audio;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    /*    public String toString() {
        return "User [name = " + first_name + " " + last_name + "; sex =" + sex
            + "; home_town = " + city + "]"
            + "bdate = " + bdate;
    }*/

    public User() {

    }

    public User(long id, String first_name, String last_name, String sex, String bdate, String city) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.sex = sex;
        this.bdate = bdate;
        this.city = city;
    }

}
