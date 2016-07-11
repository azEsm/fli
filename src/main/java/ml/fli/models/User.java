package ml.fli.models;


public class User {
    private String id;

    private String first_name;

    private String last_name;

    private String sex;

    private String home_town;

    private String bdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getHome_town() {
        return home_town;
    }

    public void setHome_town(String home_town) {
        this.home_town = home_town;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String toString() {
        return "User [name = " + first_name + " " + last_name + "; sex =" + sex
            + "; home_town = " + home_town + "]"
            + "bdate = " + bdate;
    }
}
