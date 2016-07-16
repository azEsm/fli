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

    public FrontendResponseOneUser(String userUrl, String userName, String userPic, int weight) {
        this.userUrl = userUrl;
        this.userName = userName;
        this.userPic = userPic;
        this.weight = weight;
    }

}
