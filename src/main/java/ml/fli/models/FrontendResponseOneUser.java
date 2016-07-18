package ml.fli.models;

public class FrontendResponseOneUser {

    private String userUrl;

    private String userName;

    private String userPic;

    private String weight;

    public String getUserUrl() {
        return userUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public String getWeight() {
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

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public FrontendResponseOneUser(String userUrl, String userName, String userPic, String weight) {
        this.userUrl = userUrl;
        this.userName = userName;
        this.userPic = userPic;
        this.weight = weight;
    }

    public FrontendResponseOneUser() {

    }

}
