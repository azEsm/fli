package ml.fli.models;

public class FrontendRequest {
    private String userId;

    //TODO enum
    /**
     * 0 - не задан
     * 1 - Ж
     * 2 - М
     */
    private String sex;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
