package ml.fli.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.fli.models.User;

import java.util.HashSet;
import java.util.Set;

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
        if (user.getAsJsonObject("city") instanceof JsonObject) {
            userResult.setCity(user.getAsJsonObject("city").get("id").getAsString());
        }

        return userResult;
    }

    public Set<User> parseUsers(String vkApiListUsers) {
        Set<User> listUsers = new HashSet<>();

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
            if (user.getAsJsonObject("city") instanceof JsonObject) {
                oneUser.setCity(user.getAsJsonObject("city").get("id").getAsString());
            }
            if (user.get("photo_400_orig") instanceof JsonElement) {
                oneUser.setPhoto_400_orig(user.get("photo_400_orig").getAsString());
            }
            listUsers.add(oneUser);
        }

        return listUsers;
    }

    public Set<User> parseExecuteUsers(String vkApiListUsers) {
        Set<User> resultListUser = new HashSet<>();

        JsonElement element = parser.parse(vkApiListUsers);
        JsonArray response = element.getAsJsonObject().getAsJsonArray("response");

        for (int i = 0; i < response.size(); i++) {
            User oneUser = new User();
            JsonObject object = response.get(0).getAsJsonObject();
            JsonObject user = object.getAsJsonObject("user");
            oneUser.setId(user.get("id").getAsString());
            oneUser.setFirst_name(user.get("first_name").getAsString());
            oneUser.setLast_name(user.get("last_name").getAsString());
            oneUser.setSex(user.get("sex").getAsString());
            if (user.get("bdate") instanceof JsonElement) {
                oneUser.setBdate(user.get("bdate").getAsString());
            }
            if (user.getAsJsonObject("city") instanceof JsonObject) {
                oneUser.setCity(user.getAsJsonObject("city").get("id").getAsString());
            }
            if (user.get("photo_400_orig") instanceof JsonElement) {
                oneUser.setPhoto_400_orig(user.get("photo_400_orig").getAsString());
            }
            if (object.get("audio").isJsonObject()) {
                Set<String> audioList = new HashSet<>();
                JsonArray audios = object.getAsJsonObject("audio").getAsJsonArray("items");
                for (int j = 0; j < audios.size(); j++) {
                    audioList.add(audios.get(j).getAsJsonObject().get("artist").getAsString());
                }
                oneUser.setAudio(audioList);
            }
            int count = object.getAsJsonObject("group").get("count").getAsInt();
            if (count != 0) {
                Set<String> groupsList = new HashSet<>();
                JsonArray groups = object.getAsJsonObject("group").getAsJsonArray("items");
                for (int j = 0; j < groups.size(); j++) {
                    groupsList.add(groups.get(j).getAsJsonObject().get("name").getAsString());
                }
                oneUser.setGroups(groupsList);
            }
            resultListUser.add(oneUser);
        }
        return resultListUser;
    }
}
