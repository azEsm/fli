package ml.fli.models;

public class FrontendResponseOneUser {

    private String userUrl;

    private String userName;

    private String userPic;

    private int weight;

    public String getUserUrl() {
        return userUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public int getWeight() {
        return weight;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public FrontendResponseOneUser(String userUrl, String userName, String userPic, int weight) {
        this.userUrl = userUrl;
        this.userName = userName;
        this.userPic = userPic;
        this.weight = weight;
    }

    public FrontendResponseOneUser() {

    }

}
