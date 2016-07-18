package ml.fli.models;

import java.util.ArrayList;
import java.util.List;

public class FrontendResponse {

    private ArrayList<FrontendResponseOneUser> listUser;

    public FrontendResponse(ArrayList<FrontendResponseOneUser> listUser) {
        this.listUser = listUser;
    }

    public FrontendResponse() {

    }
}
