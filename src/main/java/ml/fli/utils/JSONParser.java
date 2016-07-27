package ml.fli.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.fli.models.User;
import ml.fli.models.weka.VKAudio;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private static final JsonParser parser = new JsonParser();

    public User parseUser(String vkApiUser) {
        User userResult = new User();

        JsonElement element = parser.parse(vkApiUser);
        JsonArray response = element.getAsJsonObject().getAsJsonArray("response");
        JsonObject user = response.get(0).getAsJsonObject();
        userResult.setId(user.get("id").getAsInt());
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

    public List<User> parseUsers(String vkApiListUsers) {
        List<User> listUsers = new ArrayList<>();

        JsonElement element = parser.parse(vkApiListUsers);
        JsonObject response = element.getAsJsonObject().getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");
        for (int i = 0; i < items.size(); i++) {
            User oneUser = new User();
            JsonObject user = items.get(i).getAsJsonObject();
            oneUser.setId(user.get("id").getAsInt());
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

    public List<VKAudio> parseAudio(String vkApiListAudio)
    {
        List<VKAudio>  ListAudios = new ArrayList<>();

        JsonElement element = parser.parse(vkApiListAudio);
        JsonObject response = element.getAsJsonObject().getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");
        for (int i = 0; i < items.size(); i++) {
            JsonObject jsonAudio = items.get(i).getAsJsonObject();
            VKAudio audio = new VKAudio( jsonAudio.get("artist").getAsString(),
                    jsonAudio.get("title").getAsString());

            ListAudios.add(audio);
        }

        return ListAudios;
    }

    public List<String> parseVKGroups(String vkApiListGroups)
    {
        List<String>  listGroups = new ArrayList<>();

        JsonElement element = parser.parse(vkApiListGroups);
        JsonObject response = element.getAsJsonObject().getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");
        for (int i = 0; i < items.size(); i++) {
            String group = items.get(i).getAsString();
            listGroups.add(group);
        }

        return listGroups;
    }
}
