package ml.fli.models.weka;

public class Header {
    private String relation = "users";

    private Attributes attributes = new Attributes();

    public String getRelation() {
        return relation;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public static class Attributes {
        private String id = "";

        private String first_name = "";

        private String last_name = "";

        private String sex = "";

        private String home_town = "";

        private String bdate = "";

    }
}
