package ml.fli.utils;

import ml.fli.models.VkApiParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class VkApi {

    private static final String API_VERSION = "5.2";
    private static final String accessToken =
            "03ffa07f3a99fee7ce0bad8851ec004f7a516b48da91b4c02c7239aaa4acca65f90a5d5e918e5f19a0e1b";

    private static final String API_REQUEST = "https://api.vk.com/method/{METHOD_NAME}"
            + "?{PARAMETERS}"
            + "&access_token=" + accessToken
            + "&v=" + API_VERSION;

    public String getUser(int userId) throws IOException {
        return invokeApi("users.get", Params.create()
                .add("user_id", String.valueOf(userId))
                .add("fields", "sex,bdate,city"));
    }

    public String getUsersList(VkApiParams param) throws IOException {
        Params parameter = Params.create().add("count","1000");
        String value = param.getItem("city");
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

    public String getUserAudios(int userId, int count) throws IOException {
        return invokeApi("audio.get", Params.create()
                .add("count", String.valueOf(count))
                .add("owner_id", String.valueOf(userId)));
    }

    public String getUserGroups(int userId, int count) throws IOException {
        return invokeApi("groups.get", Params.create()
                .add("count", String.valueOf(count))
                .add("user_id", String.valueOf(userId)));
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
