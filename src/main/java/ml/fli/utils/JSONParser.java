package ml.fli.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import ml.fli.models.User;

import java.util.ArrayList;

public class JSONParser {

    private static final JsonParser parser = new JsonParser();

    public User parseUser(String vkApiUser) {
        User userResult = new User();

        JsonElement element = parser.parse(vkApiUser);
        JsonArray response = element.getAsJsonObject().getAsJsonArray("response");
        JsonObject user = response.get(0).getAsJsonObject();
        userResult.setId(user.get("id").getAsString());
        userResult.setFirst_name(user.get("first_name").getAsString());
        userResult.setLast_name(user.get("last_name").getAsString());
        userResult.setSex(user.get("sex").getAsString());
        if (user.get("bdate") instanceof JsonElement) {
            userResult.setBdate(user.get("bdate").getAsString());
        }
        if (user.get("city") instanceof JsonElement) {
            userResult.setCity(user.get("city").getAsString());
        }

        return userResult;
    }

    public ArrayList<User> parseUsers(String vkApiListUsers) {
        ArrayList<User> listUsers = new ArrayList<>();

        JsonElement element = parser.parse(vkApiListUsers);
        JsonObject response = element.getAsJsonObject().getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");
        for (int i = 0; i < items.size(); i++) {
            User oneUser = new User();
            JsonObject user = items.get(i).getAsJsonObject();
            oneUser.setId(user.get("id").getAsString());
            oneUser.setFirst_name(user.get("first_name").getAsString());
            oneUser.setLast_name(user.get("last_name").getAsString());
            oneUser.setSex(user.get("sex").getAsString());
            if (user.get("bdate") instanceof JsonElement) {
                oneUser.setBdate(user.get("bdate").getAsString());
            }
            if (user.get("city") instanceof JsonElement) {
                oneUser.setCity(user.get("city").getAsString());
            }
            if (user.get("photo_400_orig") instanceof JsonElement) {
                oneUser.setPhoto_400_orig(user.get("photo_400_orig").getAsString());
            }
            listUsers.add(oneUser);
        }

        return listUsers;
    }
}