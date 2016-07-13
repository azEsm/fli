package ml.fli.models;

import java.util.HashMap;
import java.util.Map;

public class VkApiParams {

    public static VkApiParams create() {
        return new VkApiParams();
    }

    private final HashMap<String, String> params;

    private VkApiParams() {
        params = new HashMap<>();
    }

    public VkApiParams add(String param, String value) {
        params.put(param, value);
        return this;
    }

    public String getItem(String name) {
        if (params.containsKey(name) == true) {
            for (Map.Entry entry : params.entrySet()) {
                if (entry.getKey() == name)
                    return entry.getValue().toString();
            };
        }
        return "";
    }
}
