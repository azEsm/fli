package ml.fli.models;

import ml.fli.db.models.User;

import java.util.List;

public class Response {
    private Response response;

    private int count;

    private List<User> items;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }
}
