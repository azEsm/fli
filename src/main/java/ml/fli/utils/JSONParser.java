package ml.fli.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.fli.db.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSONParser {

    private static final JsonParser parser = new JsonParser();

    public User parseUser(String vkApiUser) {
        User userResult = new User();

        JsonElement element = parser.parse(vkApiUser);
        JsonArray response = element.getAsJsonObject().getAsJsonArray("response");
        JsonObject user = response.get(0).getAsJsonObject();
        userResult.setId(user.get("id").getAsLong());
        userResult.setFirst_name(user.get("first_name").getAsString());
        userResult.setLast_name(user.get("last_name").getAsString());
        userResult.setSex(user.get("sex").getAsString());
        if (user.get("bdate") != null) {
            userResult.setBdate(user.get("bdate").getAsString());
        }
        if (user.getAsJsonObject("city") != null) {
            userResult.setCity(user.getAsJsonObject("city").get("id").getAsString());
        }
        if (user.get("about") != null) {
            userResult.setAbout(user.get("about").getAsString());
        }
        if (user.get("activities") != null) {
            userResult.setActivities(user.get("activities").getAsString());
        }
        if (user.get("books") != null) {
            userResult.setBooks(user.get("books").getAsString());
        }
        if (user.get("games") != null) {
            userResult.setGames(user.get("games").getAsString());
        }
        if (user.get("interests") != null) {
            userResult.setInterests(user.get("interests").getAsString());
        }
        if (user.get("movies") != null) {
            userResult.setMovies(user.get("movies").getAsString());
        }
        if (user.get("music") != null) {
            userResult.setMusic(user.get("music").getAsString());
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
            oneUser.setId(user.get("id").getAsLong());
            oneUser.setFirst_name(user.get("first_name").getAsString());
            oneUser.setLast_name(user.get("last_name").getAsString());
            oneUser.setSex(user.get("sex").getAsString());
            if (user.get("bdate") != null) {
                oneUser.setBdate(user.get("bdate").getAsString());
            }
            if (user.getAsJsonObject("city") != null) {
                oneUser.setCity(user.getAsJsonObject("city").get("id").getAsString());
            }
            if (user.get("photo_400_orig") != null) {
                oneUser.setPhoto_400_orig(user.get("photo_400_orig").getAsString());
            }
            if (user.get("about") != null) {
                oneUser.setAbout(user.get("about").getAsString());
            }
            if (user.get("activities") != null) {
                oneUser.setActivities(user.get("activities").getAsString());
            }
            if (user.get("books") != null) {
                oneUser.setBooks(user.get("books").getAsString());
            }
            if (user.get("games") != null) {
                oneUser.setGames(user.get("games").getAsString());
            }
            if (user.get("interests") != null) {
                oneUser.setInterests(user.get("interests").getAsString());
            }
            if (user.get("movies") != null) {
                oneUser.setMovies(user.get("movies").getAsString());
            }
            if (user.get("music") != null) {
                oneUser.setMusic(user.get("music").getAsString());
            }
            listUsers.add(oneUser);
        }

        return listUsers;
    }

    public User[] parseAudioAndGroups(String vkApiText, User[] arrayUsers) {
        JsonElement element = parser.parse(vkApiText);

        JsonArray response = element.getAsJsonObject().getAsJsonArray("response");

        for (int i = 0; i < response.size(); i++) {
            JsonObject object = response.get(i).getAsJsonObject();

            if (object.get("audio")instanceof JsonObject) {
                int audioCount = object.getAsJsonObject("audio").get("count").getAsInt();
                if (audioCount != 0) {
                    Set<String> audioList = new HashSet<>();
                    JsonArray audios = object.getAsJsonObject("audio").getAsJsonArray("items");
                    for (int j = 0; j < audios.size(); j++) {
                        audioList.add(audios.get(j).getAsJsonObject().get("artist").getAsString());
                    }
                    arrayUsers[i].setAudio(audioList);
                }
            }
            int groupCount = object.getAsJsonObject("group").get("count").getAsInt();
            if (groupCount != 0) {
                Set<String> groupsList = new HashSet<>();
                JsonArray groups = object.getAsJsonObject("group").getAsJsonArray("items");
                for (int j = 0; j < groups.size(); j++) {
                    groupsList.add(groups.get(j).getAsJsonObject().get("name").getAsString());
                }
                arrayUsers[i].setGroups(groupsList);
            }
        }
        return arrayUsers;
    }

    public Set<User> parseExecuteUsers(String vkApiListUsers) {
        Set<User> resultListUser = new HashSet<>();

        JsonElement element = parser.parse(vkApiListUsers);
        JsonArray response = element.getAsJsonObject().getAsJsonArray("response");

        for (int i = 0; i < response.size(); i++) {
            User oneUser = new User();
            JsonObject object = response.get(i).getAsJsonObject();
            JsonObject user = object.getAsJsonObject("user");
            oneUser.setId(user.get("id").getAsLong());
            oneUser.setFirst_name(user.get("first_name").getAsString());
            oneUser.setLast_name(user.get("last_name").getAsString());
            oneUser.setSex(user.get("sex").getAsString());
            if (user.get("bdate") != null) {
                oneUser.setBdate(user.get("bdate").getAsString());
            }
            if (user.getAsJsonObject("city") != null) {
                oneUser.setCity(user.getAsJsonObject("city").get("id").getAsString());
            }
            if (user.get("photo_400_orig") != null) {
                oneUser.setPhoto_400_orig(user.get("photo_400_orig").getAsString());
            }
            if (user.get("about") != null) {
                oneUser.setAbout(user.get("about").getAsString());
            }
            if (user.get("activities") != null) {
                oneUser.setActivities(user.get("activities").getAsString());
            }
            if (user.get("books") != null) {
                oneUser.setBooks(user.get("books").getAsString());
            }
            if (user.get("games") != null) {
                oneUser.setGames(user.get("games").getAsString());
            }
            if (user.get("interests") != null) {
                oneUser.setInterests(user.get("interests").getAsString());
            }
            if (user.get("movies") != null) {
                oneUser.setMovies(user.get("movies").getAsString());
            }
            if (user.get("music") != null) {
                oneUser.setMusic(user.get("music").getAsString());
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

    public List<String> parseAudio(String vkApiListAudio)
    {
        List<String>  ListAudios = new ArrayList<>();

        JsonElement element = parser.parse(vkApiListAudio);
        JsonObject response = element.getAsJsonObject().getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");
        for (int i = 0; i < items.size(); i++) {
            JsonObject jsonAudio = items.get(i).getAsJsonObject();
            String audio = jsonAudio.get("artist").getAsString() + ":" //
                    + jsonAudio.get("title").getAsString();

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
