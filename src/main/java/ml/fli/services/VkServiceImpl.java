package ml.fli.services;

import com.google.common.base.Strings;
import ml.fli.models.VkApiParams;
import ml.fli.utils.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

@Service
public final class VkServiceImpl implements VkService {

    @Autowired
    ScheduledExecutorService executorService;

    private static final String API_VERSION = "5.53";
    private static final String accessToken =
        "03ffa07f3a99fee7ce0bad8851ec004f7a516b48da91b4c02c7239aaa4acca65f90a5d5e918e5f19a0e1b";

    private static final String API_REQUEST = "https://api.vk.com/method/{METHOD_NAME}"
        + "?{PARAMETERS}"
        + "&access_token=" + accessToken
        + "&v=" + API_VERSION;

    //Заполнение параметров для получения пользователя по id
    public String getUser(String userId) {
        return invokeApi("users.get", Params.create()
            .add("user_id", userId)
            .add("fields", "sex,bdate,city,about,activities,books,games,interests,movies,music"));
    }

    //Заполнение параметров для получения списка пользователя по заданным значениям
    public String getUsersList(VkApiParams param) {
        Params parameter = Params.create();
        String count = param.getItem("count");
        if (!Strings.isNullOrEmpty(count)) {
            parameter.add("count", count);
        } else {
            parameter.add("count", "1000");
        }
        String city = param.getItem("city");
        if (!Strings.isNullOrEmpty(city)) {
            parameter.add("city", city);
        }
        String sex = param.getItem("sex");
        if (!Strings.isNullOrEmpty(sex)) {
            parameter.add("sex", sex);
        }
        String age = param.getItem("age");
        if (!Strings.isNullOrEmpty(age)) {
            parameter.add("age_from", String.valueOf(Integer.valueOf(age) - 5));
            parameter.add("age_to", String.valueOf(Integer.valueOf(age) + 5));
        }
        parameter.add("fields", "sex,bdate,city,photo_400_orig,about,activities,books,games,interests,movies,music");
        return invokeApi("users.search", parameter);
    }

    //Заполнение параметров для получения списка аудиозаписей пользователя по id
    public String getUserAudios(long userId, int count) {
        return invokeApi("audio.get", Params.create()
            .add("count", String.valueOf(count))
            .add("owner_id", String.valueOf(userId)));
    }

    //Заполнение параметров для получения списка групп пользователя по id
    public String getUserGroups(long userId, int count) {
        return invokeApi("groups.get", Params.create()
            .add("count", String.valueOf(count))
            .add("user_id", String.valueOf(userId)));
    }

    //Вызов хранимой процедуры для получения 12 пользователей с аудиозаписями и группами
    public String executeUsers() {
        return invokeApi("execute.GetUsers", Params.create());
    }

    //Вызов хранимой процедуры для получения аудиозаписей и групп у 12 пользователей
    public String executeAudioAndGroup(String listId) {
        return invokeApi("execute.GetAudioAndGroup", Params.create().add("user", listId));
    }

    private String invokeApi(String method, Params params) {
        final String parameters = (params == null) ? "" : params.build();
        String reqUrl = API_REQUEST
            .replace("{METHOD_NAME}", method)
            .replace("{PARAMETERS}&", parameters);
        return invokeApi(reqUrl);
    }

    /*private static String invokeApi(String requestUrl) {
        try {
            String result =
        } catch (IOException e) {
            throw Errors.asUnchecked(e);
        }
    }*/

    private static String invokeApi(String requestUrl) {
        try {
            final StringBuilder result = new StringBuilder();
            final URL url = new URL(requestUrl);
            try (InputStream is = url.openStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                reader.lines().forEach(result::append);
            }
            return result.toString();
        } catch (IOException e) {
            throw Errors.asUnchecked(e);
        }
    }

    private static class Params {

        private final HashMap<String, String> params;

        public static Params create() {
            return new Params();
        }

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
            params.keySet().forEach(key ->
                result.append(key).append('=').append(params.get(key)).append('&')
            );
            return result.toString();
        }
    }
}
