package ml.fli;

/**
 * Created by fab on 10.07.2016.
 */
public class User {
    private String first_name;
    private String last_name;
    private int sex;
    private String home_town;
    private String bdate;



    public User(){
    //конструктор без параметров
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getSex() {
        return sex;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setHome_town(String home_town) {
        this.home_town = home_town;
    }

    public String getHome_town() {
        return home_town;
    }

    public void setBdate(String bdate) {this.bdate = bdate;}

    public String getBdate() {return bdate;}

    public String toString(){
        return "User [name = " + first_name + " " + last_name + "; sex =" + sex
                + "; home_town = " + home_town + "]"
                + "bdate = " + bdate;
    }
}
