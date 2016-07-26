package ml.fli.utils;

import ml.fli.models.VkApiParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public final class VkApi {

    private static final String API_VERSION = "5.53";
    private static final String accessToken =
            "03ffa07f3a99fee7ce0bad8851ec004f7a516b48da91b4c02c7239aaa4acca65f90a5d5e918e5f19a0e1b";

    private static final String API_REQUEST = "https://api.vk.com/method/{METHOD_NAME}"
            + "?{PARAMETERS}"
            + "&access_token=" + accessToken
            + "&v=" + API_VERSION;
    //Заполнение параметров для получения пользователя по id
    public String getUser(String userId) throws IOException {
        return invokeApi("users.get", Params.create()
                .add("user_id", userId)
                .add("fields", "sex,bdate,city"));
    }
    //Заполнение параметров для получения списка пользователя по заданным значениям
    public String getUsersList(VkApiParams param) throws IOException {
        Params parameter = Params.create();
        String value = param.getItem("count");
        if (value != "") {
            parameter.add("count", value);
        }
        else {
            parameter.add("count", "1000");
        }
        value = param.getItem("city");
        if (value != "") {
            parameter.add("city", value);
        }
        value = param.getItem("sex");
        if (value != "") {
            parameter.add("sex", value);
        }
        value = param.getItem("age");
        if (value != "") {
            parameter.add("age_from", String.valueOf(Integer.valueOf(value) - 5));
            parameter.add("age_to", String.valueOf(Integer.valueOf(value) + 5));
        }
        parameter.add("fields", "sex,bdate,city,photo_400_orig");
        return invokeApi("users.search", parameter);
    }
    //Заполнение параметров для получения списка аудиозаписей пользователя по id
    public String getUserAudios(int userId, int count) throws IOException {
        return invokeApi("audio.get", Params.create()
                .add("count", String.valueOf(count))
                .add("owner_id", String.valueOf(userId)));
    }
    //Заполнение параметров для получения списка групп пользователя по id
    public String getUserGroups(int userId, int count) throws IOException {
        return invokeApi("groups.get", Params.create()
                .add("count", String.valueOf(count))
                .add("user_id", String.valueOf(userId)));
    }
    //Вызов хранимой процедуры для получения 12 пользователей с аудиозаписями и группами
    public String executeUsers() throws IOException {
        return invokeApi("execute.GetUsers", Params.create());
    }

    private String invokeApi(String method, Params params) throws IOException {
        final String parameters = (params == null) ? "" : params.build();
        String reqUrl = API_REQUEST
                .replace("{METHOD_NAME}", method)
                .replace("{PARAMETERS}&", parameters);
        return invokeApi(reqUrl);
    }

    private static String invokeApi(String requestUrl) throws IOException {
        final StringBuilder result = new StringBuilder();
        final URL url = new URL(requestUrl);
        try (InputStream is = url.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.lines().forEach(result::append);
        }
        return result.toString();
    }

/*    private static String invokeApi(String requestUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(requestUrl,String.class);
        return result;
    }*/

    private static class Params {

        public static Params create() {
            return new Params();
        }

        private final HashMap<String, String> params;

        private Params() {
            params = new HashMap<>();
        }

        public Params add(String key, String value) {
            params.put(key, value);
            return this;
        }
        //Конструирование строки запроса
        public String build() {
            if (params.isEmpty()) return "";
            final StringBuilder result = new StringBuilder();
            params.keySet().stream().forEach(key -> {
                result.append(key).append('=').append(params.get(key)).append('&');
            });
            return result.toString();
        }
    }
}
