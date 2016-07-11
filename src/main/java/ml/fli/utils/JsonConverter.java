package ml.fli.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;


public class JsonConverter {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }
    }

    public static <T> T jsonToObject(InputStream json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }
    }

    public static String objectToJson(Object jsonObject) {
        try {
            return mapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw Errors.asUnchecked(e);
        }
    }
}
