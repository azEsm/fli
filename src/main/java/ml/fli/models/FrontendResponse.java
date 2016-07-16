package ml.fli.models;

import java.util.List;

public class FrontendResponse {

    private List<FrontendResponseOneUser> listUser;

    public FrontendResponse(List<FrontendResponseOneUser> listUser) {
        this.listUser = listUser;
    }

    public FrontendResponse() {

    }

    public void add(FrontendResponseOneUser oneUser) {
        listUser.add(oneUser);
    }
}
